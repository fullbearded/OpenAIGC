package com.opaigc.server.infrastructure.http;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Runner.dada
 * @date: 2023/03/23
 * @description: common api response package
 * @example: {
 * "status": "",  // http status code
 * "code": "",    // business code
 * "message": "", // business message
 * "data": {}     // response body
 * }
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private Integer status;
    private String code;
    private String message;
    private T data;

    public static ApiResponse success() {
        return ApiResponse.builder().code(BusinessCode.OK.getCode())
                .status(BusinessCode.OK.getHttpCode()).message(BusinessCode.OK.getMessage()).build();
    }

    public static <T> ApiResponse<T> success(T content) {
        return ApiResponse.<T>builder()
                .message(BusinessCode.OK.getMessage())
                .code(BusinessCode.OK.getCode())
                .status(BusinessCode.OK.getHttpCode())
                .data(content)
                .build();
    }

    public static ApiResponse notFound() {
        return error(BusinessCode.RECORD_NOT_FOUND);
    }

    public static ApiResponse error(int httpStatus, String code, String message) {
        return ApiResponse.builder()
                .status(httpStatus)
                .code(code)
                .message(message)
                .data(Map.of())
                .build();
    }

    public static ApiResponse error(ApiResponseCode resultCode) {
        return ApiResponse.builder()
                .status(resultCode.getHttpCode())
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(Map.of())
                .build();
    }

    public static ApiResponse error(int httpStatus, ApiResponseCode resultCode) {
        return ApiResponse.builder()
                .status(resultCode.getHttpCode())
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(Map.of())
                .build();
    }

    public static ApiResponse error(int httpStatus, ApiResponseCode resultCode, String message) {
        return ApiResponse.builder()
                .status(httpStatus)
                .code(resultCode.getCode())
                .message(message)
                .data(Map.of())
                .build();
    }

    public static <T> ApiResponse<T> error(int httpStatus, String code, String message, T data) {
        return ApiResponse.<T>builder()
                .status(httpStatus)
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse error(ApiResponseCode resultCode, String customErrorMsg) {
        return ApiResponse.builder()
                .status(resultCode.getHttpCode())
                .code(resultCode.getCode())
                .message(customErrorMsg)
                .data(Map.of())
                .build();
    }
}
