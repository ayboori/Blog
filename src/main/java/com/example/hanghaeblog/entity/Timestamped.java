package com.example.hanghaeblog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
// 추상클래스 내의 멤버변수를 컬럼으로 인식
@MappedSuperclass
// *** 클래스에 Auditing 기능을 포함
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {

    @CreatedDate // 생성 날짜가 저장된다
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP) // 날짜 타입 매핑 시 사용됨
    private LocalDateTime createdAt;

    @LastModifiedDate // 값 변경 시간이 저장된다
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;
}