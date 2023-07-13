package com.example.hanghaeblog.repository;

import com.example.hanghaeblog.entity.Post;
import com.example.hanghaeblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);

//    작성 날짜 기준 내림차순으로 정렬하기
    List<Post> findAllByOrderByCreatedAtDesc();

}
