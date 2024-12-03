package com.example.springsecurityexample.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlarmRequest {
    private Long receiverUserId;
    private Long senderUserId;
    private String message;

        // 생성자, 게터 및 세터 생략


}
