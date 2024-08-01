package com.likelion.maydayspring.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserRegisterRequest(
    @Schema(description = "아이디", example = "thisisId")
    @NotNull(message = "ID는 null 일 수 없습니다.")
    String id,
    @Schema(description = "비밀번호", example = "thisisPassword")
    @NotNull(message = "비밀번호는 null 일 수 없습니다.")
    String password,
    @Schema(description = "이름", example = "곽민재")
    String name,
    @Schema(description = "이메일", example = "abc1234@naver.com")
    String email,
    @Schema(description = "전화번호", example = "010-0000-0000")
    String phone
) {

}
