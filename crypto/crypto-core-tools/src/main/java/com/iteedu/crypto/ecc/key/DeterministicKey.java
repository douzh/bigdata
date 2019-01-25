/*
 * Copyright 2013 Matija Mazi.
 * Copyright 2014 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iteedu.crypto.ecc.key;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.iteedu.crypto.ecc.utils.Utils.HEX;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;

import org.spongycastle.math.ec.ECPoint;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.iteedu.crypto.ecc.utils.Utils;

/**
 * A deterministic key is a node in a {@link DeterministicHierarchy}. As per
 * <a href="https://github.com/bitcoin/bips/blob/master/bip-0032.mediawiki">the
 * BIP 32 specification</a> it is a pair (key, chaincode). If you know its path
 * in the tree and its chain code you can derive more keys from this. To obtain
 * one of these, you can call
 * {@link HDKeyDerivation#createMasterPrivateKey(byte[])}.
 */
public class DeterministicKey extends ECKey {

	/**
	 * Sorts deterministic keys in the order of their child number. That's
	 * <i>usually</i> the order used to derive them.
	 */
	public static final Comparator<ECKey> CHILDNUM_ORDER = new Comparator<ECKey>() {
		@Override
		public int compare(ECKey k1, ECKey k2) {
			ChildNumber cn1 = ((DeterministicKey) k1).getChildNumber();
			ChildNumber cn2 = ((DeterministicKey) k2).getChildNumber();
			return cn1.compareTo(cn2);
		}
	};

	private final DeterministicKey parent;
	private final ImmutableList<ChildNumber> childNumberPath;
	private final int depth;
	private int parentFingerprint; // 0 if this key is root node of key hierarchy

	/** 32 bytes */
	private final byte[] chainCode;

	/**
	 * Constructs a key from its components. This is not normally something you
	 * should use.
	 */
	public DeterministicKey(ImmutableList<ChildNumber> childNumberPath, byte[] chainCode, LazyECPoint publicAsPoint,
			BigInteger priv, DeterministicKey parent) {
		super(priv, compressPoint(checkNotNull(publicAsPoint)));
		checkArgument(chainCode.length == 32);
		this.parent = parent;
		this.childNumberPath = checkNotNull(childNumberPath);
		this.chainCode = Arrays.copyOf(chainCode, chainCode.length);
		this.depth = parent == null ? 0 : parent.depth + 1;
	}

	public DeterministicKey(ImmutableList<ChildNumber> childNumberPath, byte[] chainCode, ECPoint publicAsPoint,
			BigInteger priv, DeterministicKey parent) {
		this(childNumberPath, chainCode, new LazyECPoint(publicAsPoint), priv, parent);
	}

	/**
	 * Constructs a key from its components. This is not normally something you
	 * should use.
	 */
	public DeterministicKey(ImmutableList<ChildNumber> childNumberPath, byte[] chainCode, BigInteger priv,
			DeterministicKey parent) {
		super(priv, compressPoint(ECKey.publicPointFromPrivate(priv)));
		checkArgument(chainCode.length == 32);
		this.parent = parent;
		this.childNumberPath = checkNotNull(childNumberPath);
		this.chainCode = Arrays.copyOf(chainCode, chainCode.length);
		this.depth = parent == null ? 0 : parent.depth + 1;
	}

	/**
	 * Constructs a key from its components, including its public key data and
	 * possibly-redundant information about its parent key. Invoked when
	 * deserializing, but otherwise not something that you normally should use.
	 */
	public DeterministicKey(ImmutableList<ChildNumber> childNumberPath, byte[] chainCode, LazyECPoint publicAsPoint,
			DeterministicKey parent, int depth, int parentFingerprint) {
		super(null, compressPoint(checkNotNull(publicAsPoint)));
		checkArgument(chainCode.length == 32);
		this.parent = parent;
		this.childNumberPath = checkNotNull(childNumberPath);
		this.chainCode = Arrays.copyOf(chainCode, chainCode.length);
		this.depth = depth;
	}

	/**
	 * Constructs a key from its components, including its private key data and
	 * possibly-redundant information about its parent key. Invoked when
	 * deserializing, but otherwise not something that you normally should use.
	 */
	public DeterministicKey(ImmutableList<ChildNumber> childNumberPath, byte[] chainCode, BigInteger priv,
			DeterministicKey parent, int depth, int parentFingerprint) {
		super(priv, compressPoint(ECKey.publicPointFromPrivate(priv)));
		checkArgument(chainCode.length == 32);
		this.parent = parent;
		this.childNumberPath = checkNotNull(childNumberPath);
		this.chainCode = Arrays.copyOf(chainCode, chainCode.length);
		this.depth = depth;
	}

	/** Clones the key */
	public DeterministicKey(DeterministicKey keyToClone, DeterministicKey newParent) {
		super(keyToClone.priv, keyToClone.pub.get());
		this.parent = newParent;
		this.childNumberPath = keyToClone.childNumberPath;
		this.chainCode = keyToClone.chainCode;
		this.depth = this.childNumberPath.size();
	}

	/**
	 * Returns the path through some {@link DeterministicHierarchy} which reaches
	 * this keys position in the tree. A path can be written as 0/1/0 which means
	 * the first child of the root, the second child of that node, then the first
	 * child of that node.
	 */
	public ImmutableList<ChildNumber> getPath() {
		return childNumberPath;
	}

	/**
	 * Returns the path of this key as a human readable string starting with M to
	 * indicate the master key.
	 */
	public String getPathAsString() {
		return HDUtils.formatPath(getPath());
	}

	/**
	 * Return this key's depth in the hierarchy, where the root node is at depth
	 * zero. This may be different than the number of segments in the path if this
	 * key was deserialized without access to its parent.
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Returns the last element of the path returned by
	 * {@link DeterministicKey#getPath()}
	 */
	public ChildNumber getChildNumber() {
		return childNumberPath.size() == 0 ? ChildNumber.ZERO : childNumberPath.get(childNumberPath.size() - 1);
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
	 * Return the fingerprint of the key from which this key was derived, if this is
	 * a child key, or else an array of four zero-value bytes.
	 */
	public int getParentFingerprint() {
		return parentFingerprint;
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
	 * Returns the same key with the private bytes removed. May return the same
	 * instance. The purpose of this is to save memory: the private key can always
	 * be very efficiently rederived from a parent that a private key, so storing
	 * all the private keys in RAM is a poor tradeoff especially on constrained
	 * devices. This means that the returned key may still be usable for signing and
	 * so on, so don't expect it to be a true pubkey-only object! If you want that
	 * then you should follow this call with a call to {@link #dropParent()}.
	 */
	public DeterministicKey dropPrivateBytes() {
		if (isPubKeyOnly())
			return this;
		else
			return new DeterministicKey(getPath(), getChainCode(), pub, null, parent);
	}

	/**
	 * <p>
	 * Returns the same key with the parent pointer removed (it still knows its own
	 * path and the parent fingerprint).
	 * </p>
	 *
	 * <p>
	 * If this key doesn't have private key bytes stored/cached itself, but could
	 * rederive them from the parent, then the new key returned by this method won't
	 * be able to do that. Thus, using dropPrivateBytes().dropParent() on a regular
	 * DeterministicKey will yield a new DeterministicKey that cannot sign or do
	 * other things involving the private key at all.
	 * </p>
	 */
	public DeterministicKey dropParent() {
		DeterministicKey key = new DeterministicKey(getPath(), getChainCode(), pub, priv, null);
		key.parentFingerprint = parentFingerprint;
		return key;
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

	@Override
	public boolean hasPrivKey() {
		return findParentWithPrivKey() != null;
	}

	private DeterministicKey findParentWithPrivKey() {
		DeterministicKey cursor = this;
		while (cursor != null) {
			if (cursor.priv != null)
				break;
			cursor = cursor.parent;
		}
		return cursor;
	}

	/**
	 * Derives a child at the given index using hardened derivation. Note:
	 * {@code index} is not the "i" value. If you want the softened derivation, then
	 * use instead
	 * {@code HDKeyDerivation.deriveChildKey(this, new ChildNumber(child, false))}.
	 */
	public DeterministicKey derive(int child) {
		return HDKeyDerivation.deriveChildKey(this, new ChildNumber(child, true));
	}

	/**
	 * The creation time of a deterministic key is equal to that of its parent,
	 * unless this key is the root of a tree in which case the time is stored
	 * alongside the key as per normal, see {@link ECKey#getCreationTimeSeconds()}.
	 */
	@Override
	public long getCreationTimeSeconds() {
		if (parent != null)
			return parent.getCreationTimeSeconds();
		else
			return super.getCreationTimeSeconds();
	}

	/**
	 * The creation time of a deterministic key is equal to that of its parent,
	 * unless this key is the root of a tree. Thus, setting the creation time on a
	 * leaf is forbidden.
	 */
	@Override
	public void setCreationTimeSeconds(long newCreationTimeSeconds) {
		if (parent != null)
			throw new IllegalStateException("Creation time can only be set on root keys.");
		else
			super.setCreationTimeSeconds(newCreationTimeSeconds);
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
		DeterministicKey other = (DeterministicKey) o;
		return super.equals(other) && Arrays.equals(this.chainCode, other.chainCode)
				&& Objects.equal(this.childNumberPath, other.childNumberPath);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), Arrays.hashCode(chainCode), childNumberPath);
	}

	@Override
	public String toString() {
		final MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this).omitNullValues();
		helper.add("pub", Utils.HEX.encode(pub.getEncoded()));
		helper.add("chainCode", HEX.encode(chainCode));
		helper.add("path", getPathAsString());
		if (creationTimeSeconds > 0)
			helper.add("creationTimeSeconds", creationTimeSeconds);
		helper.add("isPubKeyOnly", isPubKeyOnly());
		return helper.toString();
	}
}
