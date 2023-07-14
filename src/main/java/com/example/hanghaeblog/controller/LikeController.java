package com.example.hanghaeblog.controller;

import com.example.hanghaeblog.Service.LikeService;
import com.example.hanghaeblog.entity.StatusResult;
import com.example.hanghaeblog.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {
    private LikeService likeService;

    // 사용자는 선택한 게시글에 ‘좋아요’를 할 수 있습니다.
    // 요청이 성공하면 Client 로 성공했다는 메시지, 상태코드 반환하기
    @PostMapping("/like/post/{id}")
    public StatusResult likePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.likePost(id, userDetails.getUser());
    }

    // 사용자가 이미 ‘좋아요’한 게시글에 다시 ‘좋아요’ 요청을 하면 ‘좋아요’를 했던 기록이 취소됩니다.
    // 요청이 성공하면 Client 로 성공했다는 메시지, 상태코드 반환하기
    @DeleteMapping("/like/post/{id}")
    public StatusResult deleteLikePost(@PathVariable Long id,  @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.deleteLikePost(id, userDetails.getUser());
    }


    // 사용자는 선택한 댓글에 ‘좋아요’를 할 수 있습니다.
    // 요청이 성공하면 Client 로 성공했다는 메시지, 상태코드 반환하기
    @PostMapping("/like/comment/{id}")
    public StatusResult likeReply(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.likeReply(id, userDetails.getUser());
    }

    // 사용자가 이미 ‘좋아요’한 댓글에 다시 ‘좋아요’ 요청을 하면 ‘좋아요’를 했던 기록이 취소됩니다.
    // 요청이 성공하면 Client 로 성공했다는 메시지, 상태코드 반환하기
    @DeleteMapping("/like/comment/{id}")
    public StatusResult deleteLikeReply(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.deleteLikeReply(id, userDetails.getUser());
    }
}
