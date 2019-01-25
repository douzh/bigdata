/*
 * Copyright 2011 Google Inc.
 * Copyright 2014 Andreas Schildbach
 * Copyright 2014-2016 the libsecp256k1 contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iteedu.crypto.ecc.key;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1TaggedObject;
import org.spongycastle.asn1.DERBitString;
import org.spongycastle.asn1.DLSequence;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.math.ec.FixedPointCombMultiplier;

import com.google.common.base.Objects;
import com.iteedu.crypto.ecc.utils.Utils;
import com.iteedu.crypto.sm2.EccCurve;

// TODO: Move this class to tracking compression state itself.
// The Bouncy Castle developers are deprecating their own tracking of the compression state.

/**
 * <p>Represents an elliptic EccCurve.CURVE public and (optionally) private key, usable for digital signatures but not encryption.
 * Creating a new ECKey with the empty constructor will generate a new random keypair. Other static methods can be used
 * when you already have the public or private parts. If you create a key with only the public part, you can check
 * signatures but not create them.</p>
 *
 * <p>ECKey also provides access to Bitcoin Core compatible text message signing, as accessible via the UI or JSON-RPC.
 * This is slightly different to signing raw bytes - if you want to sign your own data and it won't be exposed as
 * text to people, you don't want to use this. If in doubt, ask on the mailing list.</p>
 *
 * <p>The ECDSA algorithm supports <i>key recovery</i> in which a signature plus a couple of discriminator bits can
 * be reversed to find the public key used to calculate it. This can be convenient when you have a message and a
 * signature and want to find out who signed it, rather than requiring the user to provide the expected identity.</p>
 *
 * <p>This class supports a variety of serialization forms. The methods that accept/return byte arrays serialize
 * private keys as raw byte arrays and public keys using the SEC standard byte encoding for public keys. Signatures
 * are encoded using ASN.1/DER inside the Bitcoin protocol.</p>
 *
 * <p>A key can be <i>compressed</i> or <i>uncompressed</i>. This refers to whether the public key is represented
 * when encoded into bytes as an (x, y) coordinate on the elliptic EccCurve.CURVE, or whether it's represented as just an X
 * co-ordinate and an extra byte that carries a sign bit. With the latter form the Y coordinate can be calculated
 * dynamically, however, <b>because the binary serialization is different the address of a key changes if its
 * compression status is changed</b>. If you deviate from the defaults it's important to understand this: money sent
 * to a compressed version of the key will have a different address to the same key in uncompressed form. Whether
 * a public key is compressed or not is recorded in the SEC binary serialisation format, and preserved in a flag in
 * this class so round-tripping preserves state. Unless you're working with old software or doing unusual things, you
 * can usually ignore the compressed/uncompressed distinction.</p>
 */
public class ECKey {



    private static final SecureRandom secureRandom;

    static {
        // Init proper random number generator, as some old Android installations have bugs that make it unsecure.
        if (Utils.isAndroidRuntime())
            new LinuxSecureRandom();

        secureRandom = new SecureRandom();
    }

    // The two parts of the key. If "priv" is set, "pub" can always be calculated. If "pub" is set but not "priv", we
    // can only verify signatures not make them.
    protected final BigInteger priv;  // A field element.
    protected final LazyECPoint pub;

    // Creation time of the key in seconds since the epoch, or zero if the key was deserialized from a version that did
    // not have this field.
    protected long creationTimeSeconds;

    /**
     * Generates an entirely new keypair. Point compression is used so the resulting public key will be 33 bytes
     * (32 for the co-ordinate and 1 byte to represent the y bit).
     */
    public ECKey() {
        this(secureRandom);
    }

    /**
     * Generates an entirely new keypair with the given {@link SecureRandom} object. Point compression is used so the
     * resulting public key will be 33 bytes (32 for the co-ordinate and 1 byte to represent the y bit).
     */
    public ECKey(SecureRandom secureRandom) {
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters(EccCurve.CURVE, secureRandom);
        generator.init(keygenParams);
        AsymmetricCipherKeyPair keypair = generator.generateKeyPair();
        ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) keypair.getPrivate();
        ECPublicKeyParameters pubParams = (ECPublicKeyParameters) keypair.getPublic();
        priv = privParams.getD();
        pub = new LazyECPoint(EccCurve.getCurve(), pubParams.getQ().getEncoded(true));
        creationTimeSeconds = Utils.currentTimeSeconds();
    }

    protected ECKey( BigInteger priv, ECPoint pub) {
        this(priv, new LazyECPoint(checkNotNull(pub)));
    }

    protected ECKey( BigInteger priv, LazyECPoint pub) {
        if (priv != null) {
            checkArgument(priv.bitLength() <= 32 * 8, "private key exceeds 32 bytes: %s bits", priv.bitLength());
            // Try and catch buggy callers or bad key imports, etc. Zero and one are special because these are often
            // used as sentinel values and because scripting languages have a habit of auto-casting true and false to
            // 1 and 0 or vice-versa. Type confusion bugs could therefore result in private keys with these values.
            checkArgument(!priv.equals(BigInteger.ZERO));
            checkArgument(!priv.equals(BigInteger.ONE));
        }
        this.priv = priv;
        this.pub = checkNotNull(pub);
    }

    /**
     * Utility for compressing an elliptic EccCurve.CURVE point. Returns the same point if it's already compressed.
     * See the ECKey class docs for a discussion of point compression.
     */
    public static ECPoint compressPoint(ECPoint point) {
        return getPointWithCompression(point, true);
    }

    public static LazyECPoint compressPoint(LazyECPoint point) {
        return point.isCompressed() ? point : new LazyECPoint(compressPoint(point.get()));
    }

    /**
     * Utility for decompressing an elliptic EccCurve.CURVE point. Returns the same point if it's already compressed.
     * See the ECKey class docs for a discussion of point compression.
     */
    public static ECPoint decompressPoint(ECPoint point) {
        return getPointWithCompression(point, false);
    }

    public static LazyECPoint decompressPoint(LazyECPoint point) {
        return !point.isCompressed() ? point : new LazyECPoint(decompressPoint(point.get()));
    }

    @SuppressWarnings("deprecation")
	private static ECPoint getPointWithCompression(ECPoint point, boolean compressed) {
      if (point.isCompressed() == compressed)
          return point;
      point = point.normalize();
      BigInteger x = point.getAffineXCoord().toBigInteger();
      BigInteger y = point.getAffineYCoord().toBigInteger();
      return EccCurve.getCurve().createPoint(x, y, compressed);
    }

    /**
     * Construct an ECKey from an ASN.1 encoded private key. These are produced by OpenSSL and stored by Bitcoin
     * Core in its wallet. Note that this is slow because it requires an EC point multiply.
     */
    public static ECKey fromASN1(byte[] asn1privkey) {
        return extractKeyFromASN1(asn1privkey);
    }

    /**
     * Creates an ECKey given the private key only. The public key is calculated from it (this is slow). The resulting
     * public key is compressed.
     */
    public static ECKey fromPrivate(BigInteger privKey) {
        return fromPrivate(privKey, true);
    }

    /**
     * Creates an ECKey given the private key only. The public key is calculated from it (this is slow), either
     * compressed or not.
     */
    public static ECKey fromPrivate(BigInteger privKey, boolean compressed) {
        ECPoint point = publicPointFromPrivate(privKey);
        return new ECKey(privKey, getPointWithCompression(point, compressed));
    }

    /**
     * Creates an ECKey given the private key only. The public key is calculated from it (this is slow). The resulting
     * public key is compressed.
     */
    public static ECKey fromPrivate(byte[] privKeyBytes) {
        return fromPrivate(new BigInteger(1, privKeyBytes));
    }

    /**
     * Creates an ECKey given the private key only. The public key is calculated from it (this is slow), either
     * compressed or not.
     */
    public static ECKey fromPrivate(byte[] privKeyBytes, boolean compressed) {
        return fromPrivate(new BigInteger(1, privKeyBytes), compressed);
    }

    /**
     * Creates an ECKey that simply trusts the caller to ensure that point is really the result of multiplying the
     * generator point by the private key. This is used to speed things up when you know you have the right values
     * already. The compression state of pub will be preserved.
     */
    public static ECKey fromPrivateAndPrecalculatedPublic(BigInteger priv, ECPoint pub) {
        return new ECKey(priv, pub);
    }

    /**
     * Creates an ECKey that simply trusts the caller to ensure that point is really the result of multiplying the
     * generator point by the private key. This is used to speed things up when you know you have the right values
     * already. The compression state of the point will be preserved.
     */
    public static ECKey fromPrivateAndPrecalculatedPublic(byte[] priv, byte[] pub) {
        checkNotNull(priv);
        checkNotNull(pub);
        return new ECKey(new BigInteger(1, priv), EccCurve.getCurve().decodePoint(pub));
    }

    /**
     * Creates an ECKey that cannot be used for signing, only verifying signatures, from the given point. The
     * compression state of pub will be preserved.
     */
    public static ECKey fromPublicOnly(ECPoint pub) {
        return new ECKey(null, pub);
    }

    /**
     * Creates an ECKey that cannot be used for signing, only verifying signatures, from the given encoded point.
     * The compression state of pub will be preserved.
     */
    public static ECKey fromPublicOnly(byte[] pub) {
        return new ECKey(null, EccCurve.getCurve().decodePoint(pub));
    }

    /**
     * Returns a copy of this key, but with the public point represented in uncompressed form. Normally you would
     * never need this: it's for specialised scenarios or when backwards compatibility in encoded form is necessary.
     */
    public ECKey decompress() {
        if (!pub.isCompressed())
            return this;
        else
            return new ECKey(priv, decompressPoint(pub.get()));
    }

    /**
     * Creates an ECKey given either the private key only, the public key only, or both. If only the private key
     * is supplied, the public key will be calculated from it (this is slow). If both are supplied, it's assumed
     * the public key already correctly matches the private key. If only the public key is supplied, this ECKey cannot
     * be used for signing.
     * @param compressed If set to true and pubKey is null, the derived public key will be in compressed form.
     */
    @Deprecated
    public ECKey( BigInteger privKey,  byte[] pubKey, boolean compressed) {
        if (privKey == null && pubKey == null)
            throw new IllegalArgumentException("ECKey requires at least private or public key");
        this.priv = privKey;
        if (pubKey == null) {
            // Derive public from private.
            ECPoint point = publicPointFromPrivate(privKey);
            point = getPointWithCompression(point, compressed);
            this.pub = new LazyECPoint(point);
        } else {
            // We expect the pubkey to be in regular encoded form, just as a BigInteger. Therefore the first byte is
            // a special marker byte.
            // TODO: This is probably not a useful API and may be confusing.
            this.pub = new LazyECPoint(EccCurve.getCurve(), pubKey);
        }
    }

    /**
     * Returns true if this key doesn't have unencrypted access to private key bytes. This may be because it was never
     * given any private key bytes to begin with (a watching key), or because the key is encrypted. You can use
     * {@link #isEncrypted()} to tell the cases apart.
     */
    public boolean isPubKeyOnly() {
        return priv == null;
    }

    /**
     * Returns true if this key has unencrypted access to private key bytes. Does the opposite of
     * {@link #isPubKeyOnly()}.
     */
    public boolean hasPrivKey() {
        return priv != null;
    }

    /** Returns true if this key is watch only, meaning it has a public key but no private key. */
    public boolean isWatching() {
        return isPubKeyOnly();
    }

    /**
     * Returns public key bytes from the given private key. To convert a byte array into a BigInteger,
     * use {@code new BigInteger(1, bytes);}
     */
    public static byte[] publicKeyFromPrivate(BigInteger privKey, boolean compressed) {
        ECPoint point = publicPointFromPrivate(privKey);
        return point.getEncoded(compressed);
    }

    /**
     * Returns public key point from the given private key. To convert a byte array into a BigInteger,
     * use {@code new BigInteger(1, bytes);}
     */
    public static ECPoint publicPointFromPrivate(BigInteger privKey) {
        /*
         * TODO: FixedPointCombMultiplier currently doesn't support scalars longer than the group order,
         * but that could change in future versions.
         */
        if (privKey.bitLength() > EccCurve.CURVE.getN().bitLength()) {
            privKey = privKey.mod(EccCurve.CURVE.getN());
        }
        return new FixedPointCombMultiplier().multiply(EccCurve.CURVE.getG(), privKey);
    }

    /**
     * Gets the raw public key value. This appears in transaction scriptSigs. Note that this is <b>not</b> the same
     * as the pubKeyHash/address.
     */
    public byte[] getPubKey() {
        return pub.getEncoded();
    }

    /** Gets the public key in the form of an elliptic EccCurve.CURVE point object from Bouncy Castle. */
    public ECPoint getPubKeyPoint() {
        return pub.get();
    }

    /**
     * Gets the private key in the form of an integer field element. The public key is derived by performing EC
     * point addition this number of times (i.e. point multiplying).
     *
     * @throws java.lang.IllegalStateException if the private key bytes are not available.
     */
    public BigInteger getPrivKey() {
        if (priv == null)
            throw new MissingPrivateKeyException();
        return priv;
    }

    /**
     * Returns whether this key is using the compressed form or not. Compressed pubkeys are only 33 bytes, not 64.
     */
    public boolean isCompressed() {
        return pub.isCompressed();
    }

    /**
     * Returns true if the given pubkey is canonical, i.e. the correct length taking into account compression.
     */
    public static boolean isPubKeyCanonical(byte[] pubkey) {
        if (pubkey.length < 33)
            return false;
        if (pubkey[0] == 0x04) {
            // Uncompressed pubkey
            if (pubkey.length != 65)
                return false;
        } else if (pubkey[0] == 0x02 || pubkey[0] == 0x03) {
            // Compressed pubkey
            if (pubkey.length != 33)
                return false;
        } else
            return false;
        return true;
    }

    private static ECKey extractKeyFromASN1(byte[] asn1privkey) {
        // To understand this code, see the definition of the ASN.1 format for EC private keys in the OpenSSL source
        // code in ec_asn1.c:
        //
        // ASN1_SEQUENCE(EC_PRIVATEKEY) = {
        //   ASN1_SIMPLE(EC_PRIVATEKEY, version, LONG),
        //   ASN1_SIMPLE(EC_PRIVATEKEY, privateKey, ASN1_OCTET_STRING),
        //   ASN1_EXP_OPT(EC_PRIVATEKEY, parameters, ECPKPARAMETERS, 0),
        //   ASN1_EXP_OPT(EC_PRIVATEKEY, publicKey, ASN1_BIT_STRING, 1)
        // } ASN1_SEQUENCE_END(EC_PRIVATEKEY)
        //
        try {
            ASN1InputStream decoder = new ASN1InputStream(asn1privkey);
            DLSequence seq = (DLSequence) decoder.readObject();
            checkArgument(decoder.readObject() == null, "Input contains extra bytes");
            decoder.close();

            checkArgument(seq.size() == 4, "Input does not appear to be an ASN.1 OpenSSL EC private key");

            checkArgument(((ASN1Integer) seq.getObjectAt(0)).getValue().equals(BigInteger.ONE),
                    "Input is of wrong version");

            byte[] privbits = ((ASN1OctetString) seq.getObjectAt(1)).getOctets();
            BigInteger privkey = new BigInteger(1, privbits);

            ASN1TaggedObject pubkey = (ASN1TaggedObject) seq.getObjectAt(3);
            checkArgument(pubkey.getTagNo() == 1, "Input has 'publicKey' with bad tag number");
            byte[] pubbits = ((DERBitString)pubkey.getObject()).getBytes();
            checkArgument(pubbits.length == 33 || pubbits.length == 65, "Input has 'publicKey' with invalid length");
            int encoding = pubbits[0] & 0xFF;
            // Only allow compressed(2,3) and uncompressed(4), not infinity(0) or hybrid(6,7)
            checkArgument(encoding >= 2 && encoding <= 4, "Input has 'publicKey' with invalid encoding");

            // Now sanity check to ensure the pubkey bytes match the privkey.
            boolean compressed = (pubbits.length == 33);
            ECKey key = new ECKey(privkey, null, compressed);
            if (!Arrays.equals(key.getPubKey(), pubbits))
                throw new IllegalArgumentException("Public key in ASN.1 structure does not match private key.");
            return key;
        } catch (IOException e) {
            throw new RuntimeException(e);  // Cannot happen, reading from memory stream.
        }
    }

    /**
     * Returns a 32 byte array containing the private key.
     * @throws com.iteedu.crypto.ecc.key.ECKey.MissingPrivateKeyException if the private key bytes are missing/encrypted.
     */
    public byte[] getPrivKeyBytes() {
        return Utils.bigIntegerToBytes(getPrivKey(), 32);
    }


    /**
     * Returns the creation time of this key or zero if the key was deserialized from a version that did not store
     * that data.
     */
    public long getCreationTimeSeconds() {
        return creationTimeSeconds;
    }

    /**
     * Sets the creation time of this key. Zero is a convention to mean "unavailable". This method can be useful when
     * you have a raw key you are importing from somewhere else.
     */
    public void setCreationTimeSeconds(long newCreationTimeSeconds) {
        if (newCreationTimeSeconds < 0)
            throw new IllegalArgumentException("Cannot set creation time to negative value: " + newCreationTimeSeconds);
        creationTimeSeconds = newCreationTimeSeconds;
    }


    public static class MissingPrivateKeyException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ECKey)) return false;
        ECKey other = (ECKey) o;
        return Objects.equal(this.priv, other.priv)
                && Objects.equal(this.pub, other.pub)
                && Objects.equal(this.creationTimeSeconds, other.creationTimeSeconds);
    }

    @Override
    public int hashCode() {
        return pub.hashCode();
    }

    public String getPrivateKeyAsHex() {
        return Utils.HEX.encode(getPrivKeyBytes());
    }

    public String getPublicKeyAsHex() {
        return Utils.HEX.encode(pub.getEncoded());
    }
}
