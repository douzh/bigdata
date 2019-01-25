package com.iteedu.crypto.ecc.key;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.iteedu.crypto.ecc.utils.Utils.HEX;

import java.math.BigInteger;
import java.util.Arrays;

import org.spongycastle.math.ec.ECPoint;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.iteedu.crypto.ecc.utils.Utils;

public class AddressKey extends ECKey {

	private final DeterministicKey parent;

	private final String addressId;
	
	/** 32 bytes */
	private final byte[] chainCode;

	/**
	 * Constructs a key from its components. This is not normally something you
	 * should use.
	 */
	public AddressKey(byte[] chainCode,String addressId, LazyECPoint publicAsPoint, BigInteger priv, DeterministicKey parent) {
		super(priv, compressPoint(checkNotNull(publicAsPoint)));
		checkArgument(chainCode.length == 32);
		this.parent = parent;
		this.chainCode = Arrays.copyOf(chainCode, chainCode.length);
		this.addressId=addressId;
	}

	public AddressKey(byte[] chainCode,String addressId, ECPoint publicAsPoint, BigInteger priv, DeterministicKey parent) {
		this(chainCode,addressId, new LazyECPoint(publicAsPoint), priv, parent);
	}
	public AddressKey(byte[] chainCode,String addressId, BigInteger priv, DeterministicKey parent) {
		this(chainCode,addressId, compressPoint(ECKey.publicPointFromPrivate(priv)), priv, parent);
	}

	public String addressId() {
		return addressId;
	}
	
	/**
	 * Returns the chain code associated with this key. See the specification to
	 * learn more about chain codes.
	 */
	public byte[] getChainCode() {
		return chainCode;
	}

	public DeterministicKey getParent() {
		return parent;
	}

	/**
	 * Returns private key bytes, padded with zeros to 33 bytes.
	 * 
	 * @throws java.lang.IllegalStateException
	 *             if the private key bytes are missing.
	 */
	public byte[] getPrivKeyBytes33() {
		byte[] bytes33 = new byte[33];
		byte[] priv = getPrivKeyBytes();
		System.arraycopy(priv, 0, bytes33, 33 - priv.length, priv.length);
		return bytes33;
	}


	/**
	 * A deterministic key is considered to be 'public key only' if it hasn't got a
	 * private key part and it cannot be rederived. If the hierarchy is encrypted
	 * this returns true.
	 */
	@Override
	public boolean isPubKeyOnly() {
		return super.isPubKeyOnly() && (parent == null || parent.isPubKeyOnly());
	}

	/**
	 * Verifies equality of all fields but NOT the parent pointer (thus the same key
	 * derived in two separate hierarchy objects will equal each other.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AddressKey other = (AddressKey) o;
		return super.equals(other) && Arrays.equals(this.chainCode, other.chainCode);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), Arrays.hashCode(chainCode));
	}

	@Override
	public String toString() {
		final MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this).omitNullValues();
		helper.add("pub", Utils.HEX.encode(pub.getEncoded()));
		helper.add("chainCode", HEX.encode(chainCode));
		if (creationTimeSeconds > 0)
			helper.add("creationTimeSeconds", creationTimeSeconds);
		helper.add("isPubKeyOnly", isPubKeyOnly());
		return helper.toString();
	}
}
