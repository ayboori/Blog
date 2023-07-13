package com.example.hanghaeblog.controller;

import com.example.hanghaeblog.Service.ReplyService;
import com.example.hanghaeblog.dto.ReplyRequestDto;
import com.example.hanghaeblog.dto.ReplyResponseDto;
import com.example.hanghaeblog.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/post/{id}/reply")
    public ReplyResponseDto createReply(@PathVariable Long id, @RequestBody ReplyRequestDto requestDto, HttpServletRequest request) {
        return replyService.createReply(id, requestDto, request);
    }

    @PutMapping("/reply/{replyId}")
    public ReplyResponseDto updateReply(@PathVariable Long replyId, @RequestBody ReplyRequestDto requestDto, HttpServletRequest request) {
        return replyService.updateReply(replyId, requestDto, request);
    }

    @DeleteMapping("/reply/{replyId}")
    public Boolean deletePost(@PathVariable Long replyId, HttpServletResponse response, HttpServletRequest request) {
        return replyService.deletePost(replyId,request);
    }

}
