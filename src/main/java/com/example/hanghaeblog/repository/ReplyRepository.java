package com.example.hanghaeblog.repository;

import com.example.hanghaeblog.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    Optional<Reply> findById(Long id);
    Optional<Reply> findByUsername(String username);

    // 작성 날짜 기준 내림차순으로 정렬하기
    List<Reply> findAllByOrderByCreatedAtDesc();
}
