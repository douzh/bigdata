package com.iteedu.crypto.sm2;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.digests.SM3Digest;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.BigIntegers;

public class SM2Cipher {
	private int ct = 1;
	private ECPoint p2;
	private SM3Digest sm3keybase;
	private SM3Digest sm3c3;
	private byte[] key = new byte[32];
	private byte keyOff = 0;

	private void reset() {
		this.sm3keybase = new SM3Digest();
		this.sm3c3 = new SM3Digest();

		byte[] p = BigIntegers.asUnsignedByteArray(this.p2.normalize().getXCoord().toBigInteger());

		this.sm3keybase.update(p, 0, p.length);
		this.sm3c3.update(p, 0, p.length);

		p = BigIntegers.asUnsignedByteArray(this.p2.normalize().getYCoord().toBigInteger());

		this.sm3keybase.update(p, 0, p.length);

		this.ct = 1;
		NextKey();
	}

	private void NextKey() {
		SM3Digest sm3keycur = new SM3Digest(this.sm3keybase);
		sm3keycur.update((byte) (this.ct >> 24 & 0xFF));
		sm3keycur.update((byte) (this.ct >> 16 & 0xFF));
		sm3keycur.update((byte) (this.ct >> 8 & 0xFF));
		sm3keycur.update((byte) (this.ct & 0xFF));
		sm3keycur.doFinal(this.key, 0);
		this.keyOff = 0;
		this.ct += 1;
	}

	public ECPoint init_enc(ECPoint pubKey) {
		BigInteger k = null;
		ECPoint c1 = null;
		ECKeyPairGenerator generator = new ECKeyPairGenerator();
		ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters(EccCurve.CURVE, new SecureRandom());
		generator.init(keygenParams);
		AsymmetricCipherKeyPair key = generator.generateKeyPair();

		ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();

		ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();

		k = ecpriv.getD();
		c1 = ecpub.getQ();

		this.p2 = pubKey.multiply(k);

		reset();

		return c1;
	}

	public void encrypt(byte[] data) {
		this.sm3c3.update(data, 0, data.length);
		for (int i = 0; i < data.length; i++) {
			if (this.keyOff == this.key.length) {
				NextKey();
			}
			data[i] ^= key[(keyOff++)];
		}
	}

	public void init_dec(BigInteger userD, ECPoint c1) {
		this.p2 = c1.multiply(userD);
		reset();
	}

	public void decrypt(byte[] data) {
		for (int i = 0; i < data.length; i++) {
			if (this.keyOff == this.key.length) {
				NextKey();
			}
			data[i]  ^=key[(keyOff++)];
		}
		this.sm3c3.update(data, 0, data.length);
	}

	public void doFinal(byte[] c3) {
		byte[] p = this.p2.normalize().getYCoord().toBigInteger().toByteArray();
		this.sm3c3.update(p, 0, p.length);
		this.sm3c3.doFinal(c3, 0);
		reset();
	}
}
