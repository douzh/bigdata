package com.iteedu.crypto.sm2;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.spongycastle.crypto.CryptoException;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.BigIntegers;
import org.spongycastle.util.encoders.Hex;

/**
 * 国密SM2工具类
 * 
 */
public class SM2Utils {

	public static byte[] enc(String pubKey, byte[] plaintext) {
		return enc(EccCurve.createECPoint(pubKey), plaintext);
	}

	public static byte[] enc(ECPoint pubKey, byte[] plaintext) {
		if (plaintext == null) {
			return null;
		}
		int plaintextlen = plaintext.length;
		SM2Cipher sm2c = new SM2Cipher();
		ECPoint c1 = sm2c.init_enc(pubKey);
		byte[] plaintext_ = (byte[]) plaintext.clone();
		sm2c.encrypt(plaintext_);
		byte[] c3 = new byte[32];
		sm2c.doFinal(c3);
		byte[] c1ba = c1.getEncoded(false);
		byte[] cipher = new byte[plaintextlen + 97];
		// 先写一个公钥
		System.arraycopy(c1ba, 0, cipher, 0, 65);
		// 再写加密内容
		System.arraycopy(plaintext_, 0, cipher, 65, plaintextlen);
		// 最后加个sm3签名
		System.arraycopy(c3, 0, cipher, 65 + plaintextlen, 32);

		return cipher;
	}

	public static byte[] dec(String prvKey, byte[] ciphertext) throws CryptoException {
		if (prvKey == null) {
			throw new CryptoException("私钥不能为空");
		}
		return dec(toByteArray(prvKey), ciphertext);
	}

	public static byte[] toByteArray(String hex) {
		return Hex.decode(hex);
	}

	public static byte[] dec(byte[] prvKey, byte[] ciphertext) throws CryptoException {
		if (prvKey == null) {
			throw new CryptoException("私钥不能为空");
		}
		return dec(toBigInteger(prvKey), ciphertext, true);
	}

	public static BigInteger toBigInteger(byte[] ba) {
		return BigIntegers.fromUnsignedByteArray(ba);
	}

	public static byte[] dec(BigInteger prvKey, byte[] ciphertext, boolean needverify) throws CryptoException {
		SM2Cipher sm2c = new SM2Cipher();
		// 减65个公钥数据和32位签名数据
		int cipherlen = ciphertext.length - 97;
		byte[] purecipher = new byte[cipherlen];
		// 取出加密内容
		System.arraycopy(ciphertext, 65, purecipher, 0, cipherlen);

		byte[] c1 = new byte[65];
		// 取出公钥内容
		System.arraycopy(ciphertext, 0, c1, 0, 65);
		ECPoint c1point = EccCurve.createECPoint(c1);
		sm2c.init_dec(prvKey, c1point);
		sm2c.decrypt(purecipher);
		if (needverify) {
			byte[] c3 = new byte[32];
			System.arraycopy(ciphertext, 65 + cipherlen, c3, 0, 32);

			BigInteger intc3 = toBigInteger(c3);
			byte[] c3_ = new byte[32];
			sm2c.doFinal(c3_);
			BigInteger intc3_ = toBigInteger(c3_);
			if (!intc3.equals(intc3_)) {
				throw new CryptoException("未通过C3校验，解密失败");
			}
		}
		return purecipher;
	}

	public static byte[] sign(byte[] msg, String priv) {
		return SM2Signer.sign(msg, toByteArray(priv));
	}

	public static byte[] sign(String msg, String priv) {
		return sign(sha256(msg), priv);
	}

	public static boolean verify(byte[] sign, byte[] msg, ECPoint pubKey) throws CryptoException {
		return SM2Signer.verify(sign, msg, pubKey);
	}

	public static boolean verify(byte[] sign, String msg, ECPoint pubKey) throws CryptoException {
		return verify(sign, sha256(msg), pubKey);
	}

	public static boolean verify(byte[] sign, String msg, String pubKey) throws CryptoException {
		return verify(sign, sha256(msg), EccCurve.createECPoint(pubKey));
	}

	private static byte[] sha256(String input) {
		return hash(input.getBytes());
	}

	private static byte[] hash(byte[] input) {
		return hash(input, 0, input.length);
	}

	private static byte[] hash(byte[] input, int offset, int length) {
		MessageDigest digest = newDigest();
		digest.update(input, offset, length);
		return digest.digest();
	}

	private static MessageDigest newDigest() {
		try {
			return MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
