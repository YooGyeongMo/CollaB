package com.example.springsecurityexample.member;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id")
    private Member receiverUserId; // 알림 수신자

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private Member senderUserId; // 알림 발신자

    private String message; // 알림 메시지
    private LocalDateTime timestamp; // 알림 생성 시간
    private boolean isRead = false; // 알림 읽음 여부

    // 추가적인 필드 (예: 초대된 팀 ID 또는 유형)를 포함할 수 있습니다.
}