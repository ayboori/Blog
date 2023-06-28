package com.example.hanghaeblog.controller;

import com.example.hanghaeblog.Service.UserService;
import com.example.hanghaeblog.dto.LoginRequestDto;
import com.example.hanghaeblog.dto.SignupRequestDto;
import com.example.hanghaeblog.entity.User;
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

    @PostMapping("/user/login-page")
    public String login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        // String token = userService.login(loginRequestDto, response);
        if(userService.login(loginRequestDto, response) != null){
            return "로그인 성공";
        }else{
            return "로그인 실패";
        }
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