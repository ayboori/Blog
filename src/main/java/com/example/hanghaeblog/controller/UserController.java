package com.example.hanghaeblog.controller;

import com.example.hanghaeblog.Service.UserService;
import com.example.hanghaeblog.dto.LoginRequestDto;
import com.example.hanghaeblog.dto.SignupRequestDto;
import com.example.hanghaeblog.entity.User;
import com.example.hanghaeblog.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 로그인 성공 시 토큰 발급
    @PostMapping("/login-page")
    public  ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return ResponseEntity.status(HttpStatus.OK).body("로그인 성공");
    }

    // 회원가입 API
    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid  @RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
            return ResponseEntity.status(HttpStatus.OK).body("회원가입 성공");
    }
}