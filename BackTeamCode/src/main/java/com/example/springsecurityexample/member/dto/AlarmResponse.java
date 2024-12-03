package com.example.springsecurityexample.member.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

@Getter@Setter
public class AlarmResponse {
    private Long id;
    private Long receiverUserId;
    private Long senderUserId;
    private String message;
    private LocalDateTime timestamp;
    private boolean isRead;

    // 생성자, 게터 및 세터 생략
}
