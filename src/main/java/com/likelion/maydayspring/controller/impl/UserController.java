package com.likelion.maydayspring.controller.impl;

import com.likelion.maydayspring.controller.UserApi;
import com.likelion.maydayspring.domain.Users;
import com.likelion.maydayspring.dto.request.LoginRequest;
import com.likelion.maydayspring.dto.request.UserRegisterRequest;
import com.likelion.maydayspring.dto.response.AuthResponse;
import com.likelion.maydayspring.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;
    @Override
    public ResponseEntity<AuthResponse> register(UserRegisterRequest request) {
        Users user = userService.register(request);
        AuthResponse response = AuthResponse.of(user);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AuthResponse> login(LoginRequest request) {
        Users user = userService.login(request);
        AuthResponse response = AuthResponse.of(user);
        return ResponseEntity.ok(response);
    }
}
