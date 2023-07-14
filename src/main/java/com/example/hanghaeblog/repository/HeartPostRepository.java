package com.example.hanghaeblog.repository;

import com.example.hanghaeblog.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartPostRepository extends JpaRepository<HeartPost,Long> {
    Optional<HeartPost> findById(Long id);
    Optional<HeartPost> findByReplyAndUser(User user, Post post);

}
