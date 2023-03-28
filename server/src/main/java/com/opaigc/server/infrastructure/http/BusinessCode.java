package com.opaigc.server.infrastructure.http;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Runner.dada
 * @date: 2023/03/23
 * @description: The common response code
 **/
@Getter
@AllArgsConstructor
public enum BusinessCode implements ApiResponseCode {
    OK("200", "请求成功", HttpStatus.OK.value()),

    SYSTEM_ERROR("100000", "系统错误，请稍后再试", INTERNAL_SERVER_ERROR.value()),
    INVALID_PARAMS("100001", "参数错误", BAD_REQUEST.value()),
    ACCESS_DENIED("100002", "权限错误", FORBIDDEN.value()),
    UPLOAD_FILE_TOO_LARGE("100003", "超过文件大小上限，单个文件不能超过50MB", PAYLOAD_TOO_LARGE.value()),
    UPLOAD_FILE_FAIL("100004", "文件上传失败，请重试", BAD_REQUEST.value()),
    REQ_TIMEOUT("100005", "请求超时，请重试", REQUEST_TIMEOUT.value()),
    ILLEGAL_REQUEST("100006", "非法请求", INTERNAL_SERVER_ERROR.value()),
    RECORD_NOT_FOUND("100007", "数据未找到", NOT_FOUND.value()),

    LOGIN_LOCKED("200001", "密码错误超过%s次，请%s分钟后再试", UNAUTHORIZED.value()),
    LOGIN_ACCOUNT_NOT_FOUND("200002", "账户或密码错误", UNAUTHORIZED.value()),
    ACCOUNT_BANNED("200003", "账户被禁用", UNAUTHORIZED.value()),
    LOGIN_EXPIRED("200004", "登录已过期，请重新登录！", FORBIDDEN.value()),
    ;

    private String code;

    private String message;

    private Integer httpCode;

}
