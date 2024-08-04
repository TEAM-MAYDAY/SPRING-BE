package com.likelion.maydayspring.enums;

import com.likelion.maydayspring.exception.BaseException;
import com.likelion.maydayspring.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum GenderType {
    MALE("남자"), FEMALE("여자"), NOGENDER("알수없음");

    private final String value;
    GenderType(String value) {
        this.value = value;
    }

    public static GenderType fromString(String genderType) {
        for (GenderType type : GenderType.values()) {
            if (type.getValue().equals(genderType)) {
                return type;
            }
        }
        throw new BaseException(ErrorCode.NOT_FOUND_OFFICE_TYPE);
    }
}
