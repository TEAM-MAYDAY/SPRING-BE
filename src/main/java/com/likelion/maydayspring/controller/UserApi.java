package com.likelion.maydayspring.controller;

import com.likelion.maydayspring.dto.request.LoginRequest;
import com.likelion.maydayspring.dto.request.UserRegisterRequest;
import com.likelion.maydayspring.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 API", description = "유저 관련 API")
@RestController
@RequestMapping("/api/v1")
@ApiResponse(responseCode = "200", description = "OK")
public interface UserApi {

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
        mediaType = "application/json",
        examples = {
            @ExampleObject(name = "US0001", description = "이미 존재하는 ID를 입력할 경우 발생합니다.",
                value = """
                                    {"code": "US0001", "message": "이미 존재하는 ID입니다."}
                                    """
            )
        }, schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/register")
    ResponseEntity<AuthResponse> register(
        @Valid
        @RequestBody UserRegisterRequest request
    );



    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @ApiResponse(responseCode = "404", description = "BAD REQUEST", content = @Content(
        mediaType = "application/json",
        examples = {
            @ExampleObject(name = "US0002", description = "입력한 ID에 해당하는 계정이 존재하지 않는 경우 발생합니다.",
                value = """
                                    {"code": "US0002", "message": "ID를 찾을 수 없습니다. 다시 확인해주세요."}
                                    """
            )
        }, schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(
        mediaType = "application/json",
        examples = {
            @ExampleObject(name = "US0003", description = "ID에 해당하는 비밀번호를 잘못 입력했을 때 발생합니다.",
                value = """
                                    {"code": "US0003", "message": "잘못된 비밀번호 입니다. 다시 확인해주세요."}
                                    """
            )
        }, schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(
        @Valid
        @RequestBody LoginRequest request
    );
}
