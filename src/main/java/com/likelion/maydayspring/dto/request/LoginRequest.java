package com.likelion.maydayspring.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LoginRequest(
    @Schema(description = "아이디", example = "thisisId")
    @NotNull(message = "ID는 null 일 수 없습니다.")
    String id,
    @Schema(description = "비밀번호", example = "thisisPassword")
    @NotNull(message = "비밀번호는 null 일 수 없습니다.")
    String password
) {

}
