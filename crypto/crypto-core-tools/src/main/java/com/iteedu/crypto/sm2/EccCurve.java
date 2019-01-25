package com.iteedu.crypto.sm2;

import java.math.BigInteger;

import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.ec.CustomNamedCurves;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECPoint;

import com.google.common.io.BaseEncoding;

public class EccCurve {
	// 自定义曲线参数实现
	// public BigInteger p;
	// public BigInteger a;
	// public BigInteger b;
	// public BigInteger n;
	// public BigInteger gx;
	// public BigInteger gy;
	// ECCurve fp = new ECCurve.Fp(p, a, b);
	// ECPoint g = fp.createPoint(gx, gy);
	// ECDomainParameters CURVE = new ECDomainParameters(fp, g, n);

	static X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
	public static ECDomainParameters CURVE = new ECDomainParameters(CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(),
			CURVE_PARAMS.getN(), CURVE_PARAMS.getH());

	public static ECCurve getCurve() {
		return CURVE.getCurve();
	}

	public static ECPoint createECPoint(byte[] point) {
		return CURVE.getCurve().decodePoint(point);
	}

	public static ECPoint createECPoint(BigInteger x, BigInteger y) {
		return CURVE.getCurve().createPoint(x, y);
	}

	public static ECPoint createECPoint(String pubKey) {
		BaseEncoding HEX = BaseEncoding.base16().lowerCase();
		return CURVE.getCurve().decodePoint(HEX.decode(pubKey));
	}
}
