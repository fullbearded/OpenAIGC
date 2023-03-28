package com.opaigc.server.infrastructure.http;

/**
 * @author: Runner.dada
 * @date: 2023/03/23
 * @description: Api response code
**/
public interface ApiResponseCode {

    String getCode();

    String getMessage();

    Integer getHttpCode();

}
