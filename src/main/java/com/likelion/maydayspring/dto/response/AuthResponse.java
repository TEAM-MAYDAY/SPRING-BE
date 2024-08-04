package com.likelion.maydayspring.dto.response;

import com.likelion.maydayspring.domain.Users;
import lombok.Builder;

@Builder
public record AuthResponse(
    Long userId,
    String name,
    String email,
    String phone,
    String job,
    String gender,
    String purpose
) {
    public static AuthResponse of(Users user) {
        return AuthResponse.builder()
            .userId(user.getUserId())
            .name(user.getName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .job(user.getJob())
            .gender(user.getGender().getValue())
            .purpose(user.getPurpose())
            .build();
    }
}
