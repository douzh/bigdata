package com.iteedu.crypto.sm2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.LazyECPoint;
import org.spongycastle.crypto.CryptoException;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.BigIntegers;
import org.spongycastle.util.encoders.Hex;

import com.google.common.io.BaseEncoding;
import com.iteedu.crypto.ReturnBean;

/**
 * 国密SM2工具类
 * 
 * @author douzihui
 *
 */
public class CryptoUtils4Sm2 {

	/**
	 * 解密文件
	 * 
	 * @param encFile
	 *            加密后文件
	 * @param decFile
	 *            解压后文件路径
	 * @param prvkey
	 * @return
	 */
	public static ReturnBean sm2dec(String encFile, String decFile, String prvkey) {
		byte[] prvkeybyte = toByteArray(prvkey);
		ReturnBean rrb = readByte(encFile);
		if (!rrb.isSucc()) {
			return rrb;
		}
		try {
			return writeByte(decryptBySM2(prvkeybyte, rrb.getData()), decFile);
		} catch (CryptoException e) {
			return new ReturnBean(ReturnBean.ERR_DEC, "decode error: file not found, src path=" + decFile, e);
		}
	}

	public static byte[] toByteArray(String hex) {
		return Hex.decode(hex);
	}

	/**
	 * 加密文件
	 * 
	 * @param srcFile
	 *            未加密源文件
	 * @param encFile
	 *            加密后文件路径
	 * @param pub1
	 *            公钥一
	 * @param pub2
	 *            公钥二
	 * @return
	 * @throws IOException
	 */
	public static ReturnBean sm2enc(String srcFile, String encFile, String pubKey) throws IOException {
		ReturnBean rrb = readByte(srcFile);
		if (!rrb.isSucc()) {
			return rrb;
		}
		byte[] ens = encryptBySM2(pubKey, rrb.getData());
		return writeByte(ens, encFile);
	}

	public static byte[] encryptBySM2(String pubKey, byte[] plaintext) {
		BaseEncoding HEX = BaseEncoding.base16().lowerCase();
		LazyECPoint lecp = new LazyECPoint(ECKey.CURVE.getCurve(), HEX.decode(pubKey));
		return encryptBySM2(lecp.get(), plaintext);
	}

	public static byte[] encryptBySM2(ECPoint pubKey, byte[] plaintext) {
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
		System.arraycopy(c1ba, 0, cipher, 0, 65);
		System.arraycopy(plaintext_, 0, cipher, 65, plaintextlen);
		System.arraycopy(c3, 0, cipher, 65 + plaintextlen, 32);

		return cipher;
	}

	public static byte[] decryptBySM2(byte[] prvKey, byte[] ciphertext) throws CryptoException {
		if (prvKey == null) {
			throw new CryptoException("私钥不能为空");
		}
		return decryptBySM2(toBigInteger(prvKey), ciphertext, true);
	}

	public static BigInteger toBigInteger(byte[] ba) {
		return BigIntegers.fromUnsignedByteArray(ba);
	}

	public static byte[] decryptBySM2(BigInteger prvKey, byte[] ciphertext, boolean needverify) throws CryptoException {
		SM2Cipher sm2c = new SM2Cipher();
		int cipherlen = ciphertext.length - 97;
		byte[] purecipher = new byte[cipherlen];
		System.arraycopy(ciphertext, 65, purecipher, 0, cipherlen);

		byte[] c1 = new byte[65];
		System.arraycopy(ciphertext, 0, c1, 0, 65);
		ECPoint c1point = SM2Cipher.createECPoint(c1);
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

	private static ReturnBean readByte(String src) {
		File f = new File(src);
		byte[] b = null;
		try {
			b = readFile(f);
		} catch (IOException e) {
			return new ReturnBean(ReturnBean.ERR_READ, "decode error: file not found, src path=" + src, e);
		}
		return new ReturnBean(ReturnBean.SUCC, "read file success, path=" + src, b);
	}

	public static byte[] readFile(File f) throws IOException {
		@SuppressWarnings("resource")
		FileChannel fc = new RandomAccessFile(f, "r").getChannel();
		try {
			MappedByteBuffer bytebuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0L, fc.size()).load();

			byte[] result = new byte[(int) fc.size()];
			if (bytebuffer.remaining() > 0) {
				bytebuffer.get(result, 0, bytebuffer.remaining());
			}
			return result;
		} catch (IOException e) {
			throw e;
		} finally {
			fc.close();
		}
	}

	private static ReturnBean writeByte(byte[] ens, String dest) {
		File f = new File(dest);
		OutputStream out = null;
		try {
			out = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			return new ReturnBean(ReturnBean.ERR_WRITE, "decode error: write file error , dest path=" + dest, e);
		}
		try {
			if (null != out) {
				out.write(ens);
			}
		} catch (Exception e) {
			return new ReturnBean(ReturnBean.ERR_WRITE, "decode error: write file error , dest path=" + dest, e);
		} finally {
			try {
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
				return new ReturnBean(ReturnBean.ERR_WRITE, "decode error: write file error , dest path=" + dest, e);
			}
		}
		return new ReturnBean(ReturnBean.SUCC, "write file success, file path=" + dest);
	}
}
