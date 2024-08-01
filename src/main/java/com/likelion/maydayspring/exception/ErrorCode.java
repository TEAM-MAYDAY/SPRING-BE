package com.likelion.maydayspring.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum  ErrorCode {
    /*
     Location 관련 error
     */
    NOT_FOUND_OFFICE_TYPE(404, "LC0001", "해당 오피스 타입을 찾을 수 없습니다."),
    NOT_FOUND_LOCATION_ID(404, "LC0002", "존재하지않는 Location ID입니다."),

    /*
     유저 관련 error
     */
    ALREADY_EXIST_ID(400, "US0001", "이미 존재하는 ID입니다."),
    NOT_FOUND_ID(404, "US0002", "ID를 찾을 수 없습니다. 다시 확인해주세요."),
    WRONG_PASSWORD(400, "US0003", "잘못된 비밀번호 입니다. 다시 확인해주세요.");


    private final int status;
    private final String code;
    private final String message;
}
