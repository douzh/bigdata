package com.iteedu.crypto.sm2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.spongycastle.crypto.CryptoException;


/**
 * 文件加解决密工具类
 *
 */
public class SM2Utils4File {

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
	public static ReturnBean dec(String encFile, String decFile, String prvkey) {
		ReturnBean rrb = readByte(encFile);
		if (!rrb.isSucc()) {
			return rrb;
		}
		try {
			return writeByte(SM2Utils.dec(prvkey, rrb.getData()), decFile);
		} catch (CryptoException e) {
			return new ReturnBean(ReturnBean.ERR_DEC, "decode error: file not found, src path=" + decFile, e);
		}
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
	public static ReturnBean enc(String srcFile, String encFile, String pubKey) throws IOException {
		ReturnBean rrb = readByte(srcFile);
		if (!rrb.isSucc()) {
			return rrb;
		}
		byte[] ens = SM2Utils.enc(pubKey, rrb.getData());
		return writeByte(ens, encFile);
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
