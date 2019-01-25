package com.iteedu.crypto.core_tool;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.spongycastle.crypto.CryptoException;

import com.iteedu.crypto.sm2.ReturnBean;
import com.iteedu.crypto.sm2.SM2Utils;
import com.iteedu.crypto.sm2.SM2Utils4File;

public class Sm2Test {

	/**
	 * 要加密的文件
	 */
	private String srcFile = "D:" + File.separator + "src.zip";

	/**
	 * 加密后的文件
	 */
	private String encFile = "D:" + File.separator + "test.enc";

	/**
	 * 解密后的文件
	 */
	private String decFile = "D:" + File.separator + "dec.zip";

	/**
	 * 加解密测试
	 * 
	 * @throws JAFCryptoException
	 * @throws IOException
	 */
	@Test
	public void testEcc() throws CryptoException, IOException {
		String pri = "ba629fd9c6824700ddba0e9905f25c2a2ac97a909c7e6a143e29ee6267004171";
		String pub = "03ec68068a48011cf4bface1f7d2ecacac3c3ff17d31beb3e5bf86acf458c723a1";

		ReturnBean rsenc = SM2Utils4File.enc(srcFile, encFile, pub);
		System.out.println(rsenc.getMsg());
		ReturnBean rsdec = SM2Utils4File.dec(encFile, decFile, pri);
		System.out.println(rsdec.getMsg());
	}

	@Test
	public void testSinger() throws CryptoException, IOException {

		String pri = "ba629fd9c6824700ddba0e9905f25c2a2ac97a909c7e6a143e29ee6267004171";
		String pub = "03ec68068a48011cf4bface1f7d2ecacac3c3ff17d31beb3e5bf86acf458c723a1";

		byte[] bsig = SM2Utils.sign("测试测试", pri);

		boolean vr = SM2Utils.verify(bsig, "测试测试", pub);

		System.out.println(vr);
	}

}
