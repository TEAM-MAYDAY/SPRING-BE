package com.likelion.maydayspring.dto.response;

import com.likelion.maydayspring.domain.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AuthResponse(
    Long userId,
    String name,
    String email,
    String phone
) {
    public static AuthResponse of(Users user) {
        return AuthResponse.builder()
            .userId(user.getUserId())
            .name(user.getName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .build();
    }
}
