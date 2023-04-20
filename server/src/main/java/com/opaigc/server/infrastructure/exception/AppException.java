package com.opaigc.server.infrastructure.exception;


import com.opaigc.server.infrastructure.http.ApiResponseCode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: Runner.dada
 * @date: 2020/12/6
 * @description: business exception for global
 **/
@Getter
@Setter
public class AppException extends RuntimeException {

	private String code;

	private String message;

	public AppException(ApiResponseCode resultCode) {
		super(String.format("AppException{code:%s, message:%s}", resultCode.getCode(), resultCode.getMessage()));
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
	}

	public AppException(ApiResponseCode resultCode, Throwable cause) {
		super(String.format("AppException{code:%s, message:%s}", resultCode.getCode(), resultCode.getMessage()), cause);
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
	}

	public AppException(String code, String message) {
		super(String.format("AppException{code:%s, message:%s}", code, message));
		this.code = code;
		this.message = message;
	}

	public AppException(String code, String message, Throwable cause) {
		super(String.format("AppException{code:%s, message:%s}", code, message), cause);
		this.code = code;
		this.message = message;
	}
}
