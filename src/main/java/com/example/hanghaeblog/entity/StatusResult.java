package com.example.hanghaeblog.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class StatusResult {
    private String msg; // 메시지
    private int code; // 응답코드

    @Builder
    public StatusResult(String msg, int code){
        this.msg = msg;
        this.code = code;
    }

}