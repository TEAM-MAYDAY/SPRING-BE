package com.likelion.maydayspring.service;

import com.likelion.maydayspring.domain.Users;
import com.likelion.maydayspring.dto.request.LoginRequest;
import com.likelion.maydayspring.dto.request.UserRegisterRequest;
import com.likelion.maydayspring.dto.response.AuthResponse;
import com.likelion.maydayspring.exception.BaseException;
import com.likelion.maydayspring.exception.ErrorCode;
import com.likelion.maydayspring.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;

    @Transactional
    public Users register(UserRegisterRequest request) {
        if (usersRepository.findByid(request.id()).isPresent()) {
            throw new BaseException(ErrorCode.ALREADY_EXIST_ID);
        }

        Users user = Users.builder()
            .id(request.id())
            .password(request.password())
            .name(request.name())
            .email(request.email())
            .phone(request.phone())
            .job(request.job())
            .gender(request.gender())
            .purpose(request.purpose())
            .build();

        return usersRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Users login(LoginRequest request) {
        Users user = usersRepository.findByid(request.id())
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_ID));
        if (!request.password().equals(user.getPassword())) {  // 비밀번호 검증 로직 변경
            throw new BaseException(ErrorCode.WRONG_PASSWORD);
        }
        return user;
    }

}
