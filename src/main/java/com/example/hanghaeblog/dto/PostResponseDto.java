package com.example.hanghaeblog.dto;


import com.example.hanghaeblog.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto{
    private String title;
    private String userName;
    private String content;
    private LocalDateTime localDate;

    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.userName = post.getUserName();
        this.content = post.getContent();
        this.localDate = post.getLocalDate();
    }
}
