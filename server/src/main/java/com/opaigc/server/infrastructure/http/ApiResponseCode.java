package com.opaigc.server.infrastructure.http;


import com.opaigc.server.infrastructure.exception.AppException;

/**
 * @author: Runner.dada
 * @date: 2020/12/6
 * @description: Api response code
 **/
public interface ApiResponseCode {

	String getCode();

	String getMessage();

	Integer getHttpCode();

	/**
	 * Used to quickly return a BizException, the default msg
	 * @return
	 */
	default AppException toException() {
		return new AppException(this);
	}

	default AppException toException(String customMsg) {
		return new AppException(getCode(), customMsg);
	}

}
