package com.iteedu.crypto.ecc.key;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.spongycastle.math.ec.ECPoint;

import com.iteedu.crypto.ecc.utils.Utils;
import com.iteedu.crypto.sm2.EccCurve;

public class AddressKeyDerivation {

	public static AddressKey derivePrivateKey(DeterministicKey parent, String addressId,
			boolean isHardend) throws HDDerivationException {
		checkArgument(parent.hasPrivKey(), "Parent key must have private key bytes for this method.");
		byte[] parentPublicKey = parent.getPubKeyPoint().getEncoded(true);
		checkState(parentPublicKey.length == 33, "Parent pubkey must be 33 bytes, but is " + parentPublicKey.length);
		byte[] address = Utils.HEX.decode(addressId);
		ByteBuffer data = ByteBuffer.allocate(33 + address.length);
		if (isHardend) {
			data.put(parent.getPrivKeyBytes33());
		} else {
			data.put(parentPublicKey);
		}
		data.put(address);
		byte[] i = HDUtils.hmacSha512(parent.getChainCode(), data.array());
		checkState(i.length == 64, i.length);
		byte[] il = Arrays.copyOfRange(i, 0, 32);
		byte[] chainCode = Arrays.copyOfRange(i, 32, 64);
		BigInteger ilInt = new BigInteger(1, il);
		assertLessThanN(ilInt, "Illegal derived key: I_L >= n");
		final BigInteger priv = parent.getPrivKey();
		BigInteger ki = priv.add(ilInt).mod(EccCurve.CURVE.getN());
		assertNonZero(ki, "Illegal derived key: derived private key equals 0.");
		return new AddressKey(chainCode, addressId, ki, parent);
	}

	public static AddressKey deriveChildKeyBytesFromPublic(DeterministicKey parent, String addressId)
			throws HDDerivationException {
		byte[] parentPublicKey = parent.getPubKeyPoint().getEncoded(true);
		checkState(parentPublicKey.length == 33, "Parent pubkey must be 33 bytes, but is " + parentPublicKey.length);
		byte[] address = Utils.HEX.decode(addressId);
		ByteBuffer data = ByteBuffer.allocate(33 + address.length);
		data.put(parentPublicKey);
		data.put(address);
		byte[] i = HDUtils.hmacSha512(parent.getChainCode(), data.array());
		checkState(i.length == 64, i.length);
		byte[] il = Arrays.copyOfRange(i, 0, 32);
		byte[] chainCode = Arrays.copyOfRange(i, 32, 64);
		BigInteger ilInt = new BigInteger(1, il);
		assertLessThanN(ilInt, "Illegal derived key: I_L >= n");

		ECPoint Ki = ECKey.publicPointFromPrivate(ilInt).add(parent.getPubKeyPoint());

		assertNonInfinity(Ki, "Illegal derived key: derived public key equals infinity.");
		return new AddressKey(chainCode, addressId, Ki, null, parent);
	}

	private static void assertNonZero(BigInteger integer, String errorMessage) {
		if (integer.equals(BigInteger.ZERO))
			throw new HDDerivationException(errorMessage);
	}

	private static void assertNonInfinity(ECPoint point, String errorMessage) {
		if (point.equals(EccCurve.getCurve().getInfinity()))
			throw new HDDerivationException(errorMessage);
	}

	private static void assertLessThanN(BigInteger integer, String errorMessage) {
		if (integer.compareTo(EccCurve.CURVE.getN()) > 0)
			throw new HDDerivationException(errorMessage);
	}
}
