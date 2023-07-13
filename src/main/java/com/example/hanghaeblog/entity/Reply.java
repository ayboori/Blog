package com.example.hanghaeblog.entity;

import com.example.hanghaeblog.dto.ReplyRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "reply")
public class Reply extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;
    @Column (nullable = false)
    private String username;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Reply(ReplyRequestDto requestDto, Post post, User user){
        this.user = user;
        this.post = post;
        this.content = requestDto.getContent();
        this.username = user.getUsername();
    }

    public void update(ReplyRequestDto replyRequestDto){
        this.content = replyRequestDto.getContent();
    }
}
