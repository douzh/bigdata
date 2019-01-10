package com.iteedu.crypto;

public class ReturnBean {
	
	public static final String SUCC="1000";
	
	public static final String ERR_READ="9001";
	
	public static final String ERR_WRITE="9011";
	
	public static final String ERR_DEC="9031";
	
	public static final String ERR_ENC="9041";

	private String code;
	private String msg;
	private byte[] data;
	private Exception err;
	
	public ReturnBean(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	public ReturnBean(String code, String msg,Exception err) {
		super();
		this.code = code;
		this.msg = msg;
		this.err=err;
	}
	
	public ReturnBean(String code, String msg, byte[] data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public Exception getErr() {
		return err;
	}

	public void setErr(Exception err) {
		this.err = err;
	}

	public boolean isSucc(){
		return SUCC.equals(code);
	}
	
}
