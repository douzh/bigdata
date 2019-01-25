package com.iteedu.crypto.sm2;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.CryptoException;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.BigIntegers;

public class SM2Signer {

	public static byte[] sign(byte[] msg, byte[] priv) {
		BigInteger userD = new BigInteger(1, priv);
		BigInteger e = new BigInteger(1, msg);
		BigInteger k = null;
		ECPoint kp = null;
		BigInteger r = null;
		BigInteger s = null;
		ECKeyPairGenerator generator = new ECKeyPairGenerator();
		ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters(EccCurve.CURVE, new SecureRandom());
		generator.init(keygenParams);
		do {
			do {
				AsymmetricCipherKeyPair keypair = generator.generateKeyPair();

				ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) keypair.getPrivate();

				ECPublicKeyParameters ecpub = (ECPublicKeyParameters) keypair.getPublic();

				k = ecpriv.getD();
				kp = ecpub.getQ();
				r = e.add(kp.normalize().getXCoord().toBigInteger());
				r = r.mod(EccCurve.CURVE.getN());
			} while ((r.equals(BigInteger.ZERO)) || (r.add(k).equals(EccCurve.CURVE.getN())));
			BigInteger da_1 = userD.add(BigInteger.ONE);
			da_1 = da_1.modInverse(EccCurve.CURVE.getN());
			s = r.multiply(userD);
			s = k.subtract(s).mod(EccCurve.CURVE.getN());
			s = da_1.multiply(s).mod(EccCurve.CURVE.getN());
		} while (s.equals(BigInteger.ZERO));
		byte[] arrayR = BigIntegers.asUnsignedByteArray(r);
		byte[] arrayS = BigIntegers.asUnsignedByteArray(s);
		byte[] bRet = new byte[arrayR.length + arrayS.length];
		System.arraycopy(arrayR, 0, bRet, 0, arrayR.length);
		System.arraycopy(arrayS, 0, bRet, arrayR.length, arrayS.length);
		return bRet;
	}

	public static boolean verify(byte[] sign, byte[] msg, ECPoint pubKey) throws CryptoException {
		if (msg == null) {
			throw new CryptoException("待验证签名原文不能为空");
		}
		if (sign == null) {
			throw new CryptoException("待验证签名不能为空");
		}
		int len = sign.length / 2;
		byte[] br = new byte[len];
		System.arraycopy(sign, 0, br, 0, len);
		byte[] bs = new byte[len];
		System.arraycopy(sign, len, bs, 0, len);

		BigInteger r = new BigInteger(1, br);
		BigInteger s = new BigInteger(1, bs);
		BigInteger e = new BigInteger(1, msg);
		BigInteger t = r.add(s).mod(EccCurve.CURVE.getN());
		if (t.equals(BigInteger.ZERO)) {
			return false;
		}
		ECPoint p = EccCurve.CURVE.getG().multiply(s);
		p = p.add(pubKey.multiply(t));
		BigInteger R = e.add(p.normalize().getXCoord().toBigInteger()).mod(EccCurve.CURVE.getN());
		return R.equals(r);
	}
}
