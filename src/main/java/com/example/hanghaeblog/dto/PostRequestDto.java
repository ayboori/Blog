package com.example.hanghaeblog.dto;


import com.example.hanghaeblog.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    // 게시글 작성 시 필요한 정보를 담는 dto

    private Long id;
    private String title;
    private String userName;
    private String content;
    private String password;
    private LocalDate localDate;

    public PostRequestDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.userName = post.getUserName();
        this.content = post.getContent();
        this.password = post.getPassword();
        this.localDate = post.getLocalDate();
    }
}
