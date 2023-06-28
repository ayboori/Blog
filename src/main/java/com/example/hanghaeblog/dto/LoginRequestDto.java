package com.example.hanghaeblog.dto;
import lombok.Getter;
import lombok.Setter;

// 로그인 요청 시 받는 내용 - dto
@Setter
@Getter
public class LoginRequestDto {
    private String username;
    private String password;
}