package com.example.hanghaeblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    // 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성
    @NotBlank
    @Size(min = 4, max = 10, message = "최소 4자 이상, 10자 이하")
    @Pattern(regexp = "^[a-z0-9]*$", message = "알파벳 소문자(a~z), 숫자(0~9)")
    private String username;

    // 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자
    @NotBlank
    @Size(min = 8, max = 15, message = "최소 8자 이상, 15자 이하")
    @Pattern(regexp = "^[a-z0-9!@#$%^&*()_+]*$", message = "비밀번호는 알파벳 소문자, 숫자, 특수문자로만 가입이 가능합니다.")
    private String password;

    // admin 임의로 true / false 변경해보며 테스트 하기
    private boolean admin = false;
    private String adminToken  = "";
}