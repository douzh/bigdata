package com.iteedu.test.bip32;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.spongycastle.crypto.CryptoException;

import com.iteedu.crypto.ReturnBean;
import com.iteedu.crypto.sm2.CryptoUtils4Sm2;

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
	public void testSm2() throws CryptoException, IOException {
		String pri = "95fdb556c229c12f7dd2fcbea0eac045b0cba74db14fa50d52edff8f38956827";
		String pub = "03481b40fe007ed1bc10fe62736187e413a59b711a947f8c76f25acac72da205ab";
		ReturnBean rsenc = CryptoUtils4Sm2.sm2enc(srcFile, encFile, pub);
		System.out.println(rsenc.getMsg());
		ReturnBean rsdec = CryptoUtils4Sm2.sm2dec(encFile, decFile, pri);
		System.out.println(rsdec.getMsg());
	}
	
}
