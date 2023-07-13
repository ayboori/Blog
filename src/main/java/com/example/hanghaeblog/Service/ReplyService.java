package com.example.hanghaeblog.Service;

import com.example.hanghaeblog.dto.ReplyRequestDto;
import com.example.hanghaeblog.dto.ReplyResponseDto;
import com.example.hanghaeblog.entity.Post;
import com.example.hanghaeblog.entity.Reply;
import com.example.hanghaeblog.entity.User;
import com.example.hanghaeblog.entity.UserRoleEnum;
import com.example.hanghaeblog.jwt.JwtUtil;
import com.example.hanghaeblog.repository.PostRepository;
import com.example.hanghaeblog.repository.ReplyRepository;
import com.example.hanghaeblog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ReplyService {

    private PostRepository postRepository;
    private ReplyRepository replyRepository;
    private UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 댓글 작성
    @Transactional
    public ReplyResponseDto createReply(Long id, ReplyRequestDto requestDto, HttpServletRequest request) {

        // 토큰 유효성 체크
        User user = checkToken(request);
        if(user == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        // 댓글을 작성하려는 Post를 id값을 기준으로 가져오기
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 글이 존재하지 않습니다.")
        );

        Reply reply = new Reply(requestDto, post, user);
        replyRepository.save(reply);

        return new ReplyResponseDto(reply);
    }


    @Transactional
    public ReplyResponseDto updateReply(Long id, ReplyRequestDto requestDto, HttpServletRequest request) {
        // 토큰 유효성 체크
        User user = checkToken(request);
        if(user == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        // 해당 메모가 DB에 존재하는지 확인
        Reply reply = replyRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("수정하려는 댓글이 존재하지 않습니다.")
        );

        // 작성자 확인, 권한 체크
        if(reply.getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN)){
            reply.update(requestDto);
            return new ReplyResponseDto(reply);
        } else {
            throw new IllegalArgumentException("댓글 작성자만 수정이 가능합니다.");
        }
    }

    public Boolean deletePost(Long id,HttpServletRequest request) {
        // 토큰 유효성 체크
        User user = checkToken(request);
        if(user == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        Reply reply = replyRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("삭제하려는 댓글이 존재하지 않습니다.")
        );

        if(reply.getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN)){
            replyRepository.delete(reply);
            return true;
        } else {
            return false;
        }
    }

    // 토큰 유효 여부 확인을 위한 함수
    public User checkToken(HttpServletRequest request){

        String token = jwtUtil.getTokenFromRequest(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return user;

        }
        return null;
    }

//    private Post findPost(Long post_id) {
//        return postRepository.findById(post_id).orElseThrow(() ->
//                new IllegalArgumentException("선택한 포스팅은 존재하지 않습니다.")
//        );
//    }
//
//    private Reply findReply(Long reply_id) {
//        return replyRepository.findById(reply_id).orElseThrow(() ->
//                new IllegalArgumentException("선택한 포스팅은 존재하지 않습니다.")
//        );
//    }
}
