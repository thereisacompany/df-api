package com.dfrecvcle.dfsystem.exception;

import org.springframework.http.HttpStatus;

/*
 * reference wiki: https://zh.wikipedia.org/wiki/HTTP%E7%8A%B6%E6%80%81%E7%A0%81
 * 500 Internal Server Error
   通用錯誤訊息，伺服器遇到了一個未曾預料的狀況，導致了它無法完成對請求的處理。沒有給出具體錯誤資訊
 * 
 */
public class InternalServerError extends BusinessRunTimeException{

	private static final long serialVersionUID = 1L;

	public InternalServerError(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}
}
