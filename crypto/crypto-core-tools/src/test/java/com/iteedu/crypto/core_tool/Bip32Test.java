package com.iteedu.crypto.core_tool;

import org.junit.Test;
import org.spongycastle.crypto.CryptoException;

import com.iteedu.crypto.ecc.key.AddressKey;
import com.iteedu.crypto.ecc.key.AddressKeyDerivation;
import com.iteedu.crypto.ecc.key.ChildNumber;
import com.iteedu.crypto.ecc.key.DeterministicKey;
import com.iteedu.crypto.ecc.key.HDKeyDerivation;
import com.iteedu.crypto.ecc.utils.Utils;
import com.iteedu.crypto.sm2.SM2Utils;

public class Bip32Test {

	@Test
	public void createMasterPrivateKeyBySeed() {
		DeterministicKey key = HDKeyDerivation.createMasterPrivateKey("hello world".getBytes());
		// key.get
		System.out.println("key.getPrivateKeyAsHex():" + key.getPrivateKeyAsHex());
		System.out.println("key.getPublicKeyAsHex():" + key.getPublicKeyAsHex());
		System.out.println("key.getPathAsString():" + key.getPathAsString());
		System.out.println("key.getPrivKey():" + key.getPrivKey());
	}

	@Test
	public void deriveChildKeyByCertNo() throws CryptoException {
		DeterministicKey root = HDKeyDerivation.createMasterPrivateKey("hello world".getBytes());
		printKey(root);
		// 派生加密密钥
		DeterministicKey c1 = HDKeyDerivation.deriveChildKey(root, 1);
		printKey(c1);
		// 用加密证件信息当作身份地址
		byte[] enCert=SM2Utils.enc(c1.getPubKeyPoint(), Utils.HEX.decode("130926198812011234"));
		System.out.println("cert address encode:"+Utils.HEX.encode(enCert));
		byte[] deCert=SM2Utils.dec(c1.getPrivKey(), enCert,true);
		System.out.println("cert address decode:"+Utils.HEX.encode(deCert));
		AddressKey ck=AddressKeyDerivation.derivePrivateKey(c1, Utils.HEX.encode(enCert),false);
		printKey(ck);
	}
	
	@Test
	public void deriveChildKey() {
		DeterministicKey root = HDKeyDerivation.createMasterPrivateKey("hello world".getBytes());
		printKey(root);
		DeterministicKey c1 = HDKeyDerivation.deriveChildKey(root, 1);
		printKey(c1);
		DeterministicKey c2 = HDKeyDerivation.deriveChildKey(root, 2);
		printKey(c2);
		DeterministicKey c11 = HDKeyDerivation.deriveChildKey(c1, 1);
		printKey(c11);
		DeterministicKey c12 = HDKeyDerivation.deriveChildKey(c1, 2);
		printKey(c12);
		DeterministicKey c21 = HDKeyDerivation.deriveChildKey(c2, 1);
		printKey(c21);
		DeterministicKey c22 = HDKeyDerivation.deriveChildKey(c2, 2);
		printKey(c22);
	}

	@Test
	public void derivePubChildKey() {
		DeterministicKey root = HDKeyDerivation.createMasterPrivateKey("hello world".getBytes());
		DeterministicKey rootPub = HDKeyDerivation.createMasterPubKeyFromBytes(root.getPubKey(), root.getChainCode());
		printKey(rootPub);
		DeterministicKey c1 = HDKeyDerivation.deriveChildKey(rootPub, 1);
		printKey(c1);
		DeterministicKey c2 = HDKeyDerivation.deriveChildKey(rootPub, 2);
		printKey(c2);
		DeterministicKey c11 = HDKeyDerivation.deriveChildKey(c1, 1);
		printKey(c11);
		DeterministicKey c12 = HDKeyDerivation.deriveChildKey(c1, 2);
		printKey(c12);
		DeterministicKey c21 = HDKeyDerivation.deriveChildKey(c2, 1);
		printKey(c21);
		DeterministicKey c22 = HDKeyDerivation.deriveChildKey(c2, 2);
		printKey(c22);
	}
	
	@Test
	public void deriveHardenChildKey() {
		DeterministicKey root = HDKeyDerivation.createMasterPrivateKey("hello world".getBytes());
		printKey(root);
		DeterministicKey c1 = HDKeyDerivation.deriveChildKey(root, new ChildNumber(1, true));
		printKey(c1);
		DeterministicKey c2 = HDKeyDerivation.deriveChildKey(root, new ChildNumber(2, true));
		printKey(c2);
		DeterministicKey c11 = HDKeyDerivation.deriveChildKey(c1, new ChildNumber(1, true));
		printKey(c11);
		DeterministicKey c12 = HDKeyDerivation.deriveChildKey(c1, new ChildNumber(2, true));
		printKey(c12);
		DeterministicKey c21 = HDKeyDerivation.deriveChildKey(c2, new ChildNumber(1, true));
		printKey(c21);
		DeterministicKey c22 = HDKeyDerivation.deriveChildKey(c2, new ChildNumber(2, true));
		printKey(c22);
	}

	@Test
	public void deriveMixChildKey() {
		DeterministicKey root = HDKeyDerivation.createMasterPrivateKey("hello world".getBytes());
		printKey(root);
		DeterministicKey c1 = HDKeyDerivation.deriveChildKey(root, new ChildNumber(1, true));
		printKey(c1);
		DeterministicKey c2 = HDKeyDerivation.deriveChildKey(root, new ChildNumber(2, false));
		printKey(c2);
		DeterministicKey c11 = HDKeyDerivation.deriveChildKey(c1, new ChildNumber(1, true));
		printKey(c11);
		DeterministicKey c12 = HDKeyDerivation.deriveChildKey(c1, new ChildNumber(2, false));
		printKey(c12);
		DeterministicKey c21 = HDKeyDerivation.deriveChildKey(c2, new ChildNumber(1, true));
		printKey(c21);
		DeterministicKey c22 = HDKeyDerivation.deriveChildKey(c2, new ChildNumber(2, false));
		printKey(c22);
	}

	public void printKey(AddressKey key) {
		if (key.hasPrivKey())
			System.out.println("cert pri:" + key.getPrivateKeyAsHex());
		else
			System.out.println("cert pri: not exist");
		
		System.out.println("cert pub:" + key.getPublicKeyAsHex());
	}
	
	public void printKey(DeterministicKey key) {
		if (key.hasPrivKey())
			System.out.println(key.getPathAsString() + ":" + "pri:" + key.getPrivateKeyAsHex());
		else
			System.out.println(key.getPathAsString() + ":" + "pri: not exist");
		
		System.out.println(key.getPathAsString() + ":" + "pub:" + key.getPublicKeyAsHex());
	}
}
