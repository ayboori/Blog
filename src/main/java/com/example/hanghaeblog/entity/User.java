package com.example.hanghaeblog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// id는 고유값 (DB에서 자동 생성, userName, password로 회원가입 구현)
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users") // 아래의 값으로 생성될 테이블의 이름 지정
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    //- EnumType을 DB 컬럼에 저장할 때 사용하는 애너테이션입니다.
    //- `EnumType.*STRING`* 옵션을 사용하면 **Enum의 이름을 DB에 그대로 저장합니다.
    //- `USER(Authority.USER)` → USER

    private UserRoleEnum role;
    public User(String username, String password, UserRoleEnum role){
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
