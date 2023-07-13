package com.example.hanghaeblog.dto;

import com.example.hanghaeblog.entity.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReplyResponseDto {
    private Long id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ReplyResponseDto(Reply reply){
        this.id = reply.getId();
        this.username = reply.getUsername();
        this.content = reply.getContent();
        this.createdAt = reply.getCreatedAt();
        this.modifiedAt = reply.getModifiedAt();
    }
}
