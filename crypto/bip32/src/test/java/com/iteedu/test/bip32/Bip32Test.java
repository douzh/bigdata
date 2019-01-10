package com.iteedu.test.bip32;

import java.math.BigInteger;
import java.security.SignatureException;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.LazyECPoint;
import org.junit.Test;
import org.spongycastle.util.BigIntegers;

import com.google.common.io.BaseEncoding;

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

	@Test
	public void createECPointByPub() {
		// seed:hello world
		String pri = "95fdb556c229c12f7dd2fcbea0eac045b0cba74db14fa50d52edff8f38956827";
		String pub = "03481b40fe007ed1bc10fe62736187e413a59b711a947f8c76f25acac72da205ab";
		BaseEncoding HEX = BaseEncoding.base16().lowerCase();
		LazyECPoint lecp = new LazyECPoint(ECKey.CURVE.getCurve(), HEX.decode(pub));
		System.out.println(HEX.encode(lecp.getEncoded()));

	}

	@Test
	public void signMessage() throws SignatureException {
		// seed:hello world
		String pri = "95fdb556c229c12f7dd2fcbea0eac045b0cba74db14fa50d52edff8f38956827";
		// String pub =
		// "03481b40fe007ed1bc10fe62736187e413a59b711a947f8c76f25acac72da205ab";
		// 67842878301387567012574581946528827920287147455426674517651607044871930865703

		// 私钥签名
		BaseEncoding HEX = BaseEncoding.base16().lowerCase();
		BigInteger privKey = BigIntegers.fromUnsignedByteArray(HEX.decode(pri));
		ECKey k = ECKey.fromPrivate(privKey);
		String msg = "hello world";
		String signm = k.signMessage(msg);
		System.out.println(signm);
		// 从签名信息中恢复公钥
		ECKey key = ECKey.signedMessageToKey(msg, signm);
		printKey(key);
		// 通过对比私钥生成的公钥与恢复的公钥验证签名
		System.out.println(key.getPublicKeyAsHex().equals(k.getPublicKeyAsHex()));
	}

	public void printKey(ECKey key) {
		if (key.hasPrivKey())
			System.out.println("pri:" + key.getPrivateKeyAsHex());
		System.out.println("pub:" + key.getPublicKeyAsHex());
	}

	public void printKey(DeterministicKey key) {
		if (key.hasPrivKey())
			System.out.println(key.getPathAsString() + ":" + "pri:" + key.getPrivateKeyAsHex());
		else
			System.out.println(key.getPathAsString() + ":" + "pri: not exist");
		
		System.out.println(key.getPathAsString() + ":" + "pub:" + key.getPublicKeyAsHex());
	}
}
