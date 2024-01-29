package com.dfrecvcle.dfsystem.utils;

public class BaseResponseInfo {
	public int code;
	public Object result;
//	public Object data;

	public BaseResponseInfo() {
		code = 400;
		result = null;
	}
}
