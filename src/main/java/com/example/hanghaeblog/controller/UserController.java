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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/user/login-page")
    public String login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String token = jwtUtil.createToken(userService.login(loginRequestDto, response)); // 로그인 수행, username 받아오기 > token 만들기
        // 헤더에 토큰 추가
        response.addHeader("Authorization", "Bearer " + token);
        return "로그인 성공";
    }

    // 회원가입 API
    @ResponseBody
    @PostMapping("/user/signup")
    public String signup(@Valid  @RequestBody SignupRequestDto requestDto) {
        User user = userService.signup(requestDto);
        if(user != null){
            return "회원가입 성공";
        }else{
            return "회원가입 실패";
        }
    }
}