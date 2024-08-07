package com.likelion.maydayspring.dto.response;

import com.likelion.maydayspring.exception.ErrorCode;

public record ErrorResponse(
    String code,

    String message
) {
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }
}
