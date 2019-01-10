package com.iteedu.test.bip32;

import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	DeterministicKey key=HDKeyDerivation.createMasterPrivateKey("helloword".getBytes());
//        key.get
    	System.out.println( "key.getPrivateKeyAsHex():"+ key.getPrivateKeyAsHex());
    	System.out.println( "key.getPublicKeyAsHex():"+ key.getPublicKeyAsHex());
    	System.out.println( "key.getPathAsString():"+ key.getPathAsString());
    	System.out.println( "key.getPrivKey():"+ key.getPrivKey());
    }
}
