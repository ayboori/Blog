package com.example.hanghaeblog.Service;

import com.example.hanghaeblog.dto.PostResponseDto;
import com.example.hanghaeblog.dto.ReplyResponseDto;
import com.example.hanghaeblog.entity.*;
import com.example.hanghaeblog.repository.HeartPostRepository;
import com.example.hanghaeblog.repository.HeartReplyRepository;
import com.example.hanghaeblog.repository.PostRepository;
import com.example.hanghaeblog.repository.ReplyRepository;
import com.example.hanghaeblog.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.sql.Like;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LikeService {
    private final HeartReplyRepository heartReplyRepository;
    private final HeartPostRepository heartPostRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;


    @Transactional
    public StatusResult likePost(Long PostId, User user) {
        // 토큰 체크
        if (user == null) {
            return new StatusResult("토큰이 유효하지 않습니다.", 400);
        }
        // 좋아요 누를 게시글 find
        Post post = postRepository.findById(PostId).orElseThrow(
                () -> new IllegalArgumentException("좋아요를 누를 게시글이 존재하지 않습니다.")
        );

        // 중복 좋아요 방지
        Optional<HeartPost> heartPost = heartPostRepository.findByReplyAndUser(user,post);
        if (heartPost != null){
            return new StatusResult("이미 해당 게시글에 좋아요를 누르셨습니다.", 400);
        }

        if (!post.getUser().equals(user) || user.getRole().equals(UserRoleEnum.ADMIN)) { // 작성자거나 ADMIN 권한일 때
            throw new IllegalArgumentException("글 작성자가 아닙니다.");
        }

        // HeartPostRepository DB저장
        heartPostRepository.save(new HeartPost(post, user));

        return new StatusResult("좋아요 완료", 200);
    }

    @Transactional
    public StatusResult deleteLikePost(Long PostId, User user) {
        // 토큰 체크

        if (user == null) {
            return new StatusResult("토큰이 유효하지 않습니다.", 400);
        }

        // 좋아요 누른 게시글 find
        Post post = postRepository.findById(PostId).orElseThrow(
                () -> new IllegalArgumentException("좋아요를 취소할 게시글이 존재하지 않습니다.")
        );

        if (!post.getUser().equals(user) || user.getRole().equals(UserRoleEnum.ADMIN)) { // 작성자거나 ADMIN 권한일 때
            throw new IllegalArgumentException("글 작성자가 아닙니다.");
        }

        heartPostRepository.delete(new HeartPost(post, user));

        return new StatusResult("좋아요 취소 성공",200);
    }


    @Transactional
    public StatusResult likeReply(Long replyId, User user) {
        // 토큰 체크
        if (user == null) {
            return new StatusResult("토큰이 유효하지 않습니다.", 400);
        }
        // 좋아요 누른 댓글 find
        Reply reply = replyRepository.findById(replyId).orElseThrow(
                () -> new IllegalArgumentException("좋아요를 누를 게시글이 존재하지 않습니다.")
        );

        // 중복 좋아요 방지
        Optional<HeartReply> heartreply = heartReplyRepository.findByReplyAndUser(user,reply);
        if (heartreply != null){
            return new StatusResult("이미 해당 게시글에 좋아요를 누르셨습니다.", 400);
        }
        // HeartReplyRepository DB저장
        heartReplyRepository.save(new HeartReply(reply, user));
        return new StatusResult("좋아요 완료", 200);
    }

    @Transactional
    public StatusResult deleteLikeReply(Long replyId, User user) {
        // 토큰 체크
        if (user == null) {
            return new StatusResult("토큰이 유효하지 않습니다.", 400);
        }

        // 좋아요 누른 댓글 find
        Reply reply = replyRepository.findById(replyId).orElseThrow(
                () -> new IllegalArgumentException("좋아요를 누를 게시글이 존재하지 않습니다.")
        );

        // 중복 좋아요 방지
        Optional<HeartReply> heartreply = heartReplyRepository.findByReplyAndUser(user,reply);
        if (heartreply != null){
            return new StatusResult("이미 해당 게시글에 좋아요를 누르셨습니다.", 400);
        }

        heartReplyRepository.delete(new HeartReply(reply, user));
        return new StatusResult("좋아요 취소", 200);
    }



}
