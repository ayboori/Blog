package com.example.hanghaeblog.Service;

import com.example.hanghaeblog.dto.LoginRequestDto;
import com.example.hanghaeblog.dto.SignupRequestDto;
import com.example.hanghaeblog.entity.User;
import com.example.hanghaeblog.entity.UserRoleEnum;
import com.example.hanghaeblog.jwt.JwtUtil;
import com.example.hanghaeblog.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto) { // 가입할 데이터 받아오기
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword()); // 암호화하여 넣기

        // 회원 중복 확인 - username에 unique 속성 있음
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) { // Optional 타입 내의 메소드
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록을 위해 객체 생성
        User user = new User(username, password,role);
        userRepository.save(user);
        return;
    }

    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 존재하는지 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if(!user.getPassword().equals(password)){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(),user.getRole()));
        return;
    }
}