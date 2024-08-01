package com.likelion.maydayspring.enums;

import com.likelion.maydayspring.exception.BaseException;
import com.likelion.maydayspring.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum OfficeType {
    CAFE("카페형 오피스"), SHARED("공유형 오피스"), UNCLASSIFIED("미분류");
    
    private final String value;
    OfficeType(String value) {
        this.value = value;
    }

    public static OfficeType fromString(String officeType) {
        for (OfficeType type : OfficeType.values()) {
            if (type.getValue().equals(officeType)) {
                return type;
            }
        }
        throw new BaseException(ErrorCode.NOT_FOUND_OFFICE_TYPE);
    }
}
