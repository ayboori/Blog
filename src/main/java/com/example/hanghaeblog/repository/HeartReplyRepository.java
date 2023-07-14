package com.example.hanghaeblog.repository;

import com.example.hanghaeblog.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.Like;

import java.util.Optional;

public interface HeartReplyRepository extends JpaRepository<HeartReply,Long> {
    Optional<HeartReply> findById(Long id);

    Optional<HeartReply> findByReplyAndUser(User user, Reply reply);
}
