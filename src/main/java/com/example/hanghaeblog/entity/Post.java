package com.example.hanghaeblog.entity;
import com.example.hanghaeblog.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.relational.core.sql.Like;

import java.util.ArrayList;
import java.util.List;

// 게시글 엔티티

@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post extends Timestamped{
    // 제목, 작성자명, 작성 내용, 작성 날짜
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false)
    private String title;
    @Column (nullable = false)
    private String content;
    @Column (nullable = false)
    private String username;
    @Column(name = "likes", nullable = false)
    private int likeCount = 0; // default = 0

    // 외래키로 user_id 받아오기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 게시글 조회 시 댓글 조회
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Reply> commentList = new ArrayList<>();

    // 게시글 조회 시 좋아요 갯수 조회
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<HeartPost> likes = new ArrayList<>();


    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
        this.username = user.getUsername();
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}