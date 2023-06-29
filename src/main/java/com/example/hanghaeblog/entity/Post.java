package com.example.hanghaeblog.entity;

import com.example.hanghaeblog.dto.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String userName;
    private String content;
    private LocalDate localDate;
    private String password;

    public Post(PostRequestDto requestDto, User user) { // 회원 이름, 비밀번호는 user에서 가져오기
        this.title = requestDto.getTitle();
        this.userName = user.getUsername();
        this.content = requestDto.getContent();
        this.password = user.getPassword();
        this.localDate = requestDto.getLocalDate();  // 현재 시간
    }

    // 수정
    public void update(PostRequestDto requestDto) {
        this.id = requestDto.getId();
        this.title = requestDto.getTitle();
        this.userName = requestDto.getUserName();
        this.content = requestDto.getContent();
        this.password = requestDto.getPassword();
        this.localDate = LocalDate.now();  // 현재 시간
    }
}
