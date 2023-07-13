package com.example.hanghaeblog.Service;

import com.example.hanghaeblog.dto.PostRequestDto;
import com.example.hanghaeblog.dto.PostResponseDto;
import com.example.hanghaeblog.entity.Post;
import com.example.hanghaeblog.entity.User;
import com.example.hanghaeblog.jwt.JwtUtil;
import com.example.hanghaeblog.repository.PostRepository;
import com.example.hanghaeblog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 게시글 작성 API
    //    - 제목, 작성자명, 비밀번호, 작성 내용을 저장하고
    //    - 저장된 게시글을 Client 로 반환하기

    // 변경 부분
    // - 토큰을 검사하여, 유효한 토큰일 경우에만 게시글 작성 가능
    //- 제목, 작성 내용을 저장
    // 저장된 게시글을 Client 로 반환하기(username은 로그인 된 사용자)
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {

        // 토큰 체크 추가
        User user = checkToken(request);
        if (user == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        // RequestDto -> Entity
        Post post = new Post(requestDto,user);

        //DB 저장
        postRepository.save(post);

        // Entity -> ResponseDto
        return new PostResponseDto(post);
    }

    // 전체 게시글 목록 조회 API
    //- 제목, 작성자명, 작성 내용, 작성 날짜를 조회하기
    //- 작성 날짜 기준 내림차순으로 정렬하기
    public List<PostResponseDto> getPosts() {
        // DB 조회 후 List 객체에 담기
        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();

        // Dto 객체에 담기 위해 List 배열 생성
        List<PostResponseDto> postResponseDto = new ArrayList<>();

        // Dto 객체에 담기
        for (Post post : postList) {
            postResponseDto.add(new PostResponseDto(post));
        }

        return postResponseDto;
    }

    // 선택한 게시글 조회 API
    //    - 선택한 게시글의 제목, 작성자명, 작성 날짜, 작성 내용을 조회하기
    //    (검색 기능이 아닙니다. 간단한 게시글 조회만 구현해주세요.)
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        // DB에서 게시글 조회
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("선택하신 게시글이 존재하지 않습니다."));

        // Entity -> ResponseDto
        return new PostResponseDto(post);
    }


    // 선택한 게시글 수정 API
    //    - 수정을 요청할 때 수정할 데이터와 비밀번호를 같이 보내서 서버에서 비밀번호 일치 여부를 확인 한 후
    //    - 제목, 작성자명, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기
    @Transactional
    public PostResponseDto updatePost(Long id,  PostRequestDto requestDto, HttpServletRequest request) {
        // 토큰 체크 추가
        User user = checkToken(request);

        if (user == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 글이 존재하지 않습니다.")
        );

        if (!post.getUser().equals(user)) {
            throw new IllegalArgumentException("글 작성자가 아닙니다.");
        }

        post.update(requestDto);
        return new PostResponseDto(post);
    }

    // 선택한 게시글 삭제 API
    //    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 삭제 가능
    //    - 선택한 게시글을 삭제하고 Client 로 성공했다는 표시 반환하기
    public String deletePost(Long id, HttpServletRequest request) {
        // 토큰 유효성 체크
        User user = checkToken(request);
        if(user == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        // 글 읽어와서 저장, 해당 메모 DB에 존재하는지 확인
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("선택하신 게시글이 존재하지 않습니다."));

        if (post.getUser().equals(user)) {
            postRepository.delete(post);
            return "삭제 성공했습니다.";
        }else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
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
}

