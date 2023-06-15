package com.example.hanghaeblog.controller;

import com.example.hanghaeblog.dto.PostRequestDto;
import com.example.hanghaeblog.dto.PostResponseDto;
import com.example.hanghaeblog.entity.Post;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PostController {
    private final Map<Long, Post> postList = new HashMap<>();

    // 게시글 작성 API
    //    - 제목, 작성자명, 비밀번호, 작성 내용을 저장하고
    //    - 저장된 게시글을 Client 로 반환하기
    @PostMapping("/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        // RequestDto -> Entity
        Post post = new Post(requestDto);

        // Post Max ID Check
        Long maxId = postList.size() > 0 ? Collections.max( postList.keySet() ) + 1 : 1;
        post.setId(maxId);

        // DB 저장
        postList.put(maxId, post);

        // Entity -> ResponseDto
        PostResponseDto PostResponseDto = new PostResponseDto(post);

        return PostResponseDto;
    }

    // 전체 게시글 목록 조회 API
    //- 제목, 작성자명, 작성 내용, 작성 날짜를 조회하기
    //- 작성 날짜 기준 내림차순으로 정렬하기
    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
        // Map To List
        List<PostResponseDto> responseList = postList.values().stream()
                .map(PostResponseDto::new)
                .sorted(Comparator.comparing(PostResponseDto::getLocalDate).reversed())
                .collect(Collectors.toList());

        return responseList;
    }

    // 선택한 게시글 조회 API
    //    - 선택한 게시글의 제목, 작성자명, 작성 날짜, 작성 내용을 조회하기
    //    (검색 기능이 아닙니다. 간단한 게시글 조회만 구현해주세요.)
    @GetMapping("/posts/{id}")
    @ResponseBody
    public PostResponseDto getPost(@PathVariable Long id) {
        PostResponseDto response = new PostResponseDto(postList.get(id));
        return response;
    }


    // 선택한 게시글 수정 API
    //    - 수정을 요청할 때 수정할 데이터와 비밀번호를 같이 보내서 서버에서 비밀번호 일치 여부를 확인 한 후
    //    - 제목, 작성자명, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기
    @PutMapping("/posts/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        if(postList.containsKey(id)) {
            // 해당 메모 가져오기
            Post post = postList.get(id);

            // Post 수정
            post.update(requestDto);
            return post.getId();
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    // 선택한 게시글 삭제 API
    //    - 삭제를 요청할 때 비밀번호를 같이 보내서 서버에서 비밀번호 일치 여부를 확인 한 후
    //    - 선택한 게시글을 삭제하고 Client 로 성공했다는 표시 반환하기
    @DeleteMapping("/posts/{id}")
    public String deletePost(@PathVariable Long id,@RequestBody String password) {
        // 해당 메모가 DB에 존재하는지 확인
        if(postList.containsKey(id)) {
            // 해당 메모 삭제하기
            postList.remove(id);
            return "삭제 성공했습니다.";
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

}