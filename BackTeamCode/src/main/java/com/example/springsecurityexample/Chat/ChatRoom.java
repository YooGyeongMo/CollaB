package com.example.springsecurityexample.Chat;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
@NoArgsConstructor
public class ChatRoom {

    private String roomId;//채팅방 아이디
    private String name;//채팅방 이름
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId,String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handleAction(WebSocketSession session, ChatDTO message, ChatService service) {
        // MessageType == ENTER ; 채팅방에 입장하면 "{UserName}님이 입장했습니다" 출력
        // MessageType == TALK ; 채팅방에 메세지 출력
        // MessageType == LEAVE ; 채팅방에 "{UserName}님이 나갔습니다" 출력
        // MessageType == Enter, MessageType == LEAVE 일때 테스트 완료 되면 TALK 일때만 남겨두자

        if (message.getType().equals(ChatDTO.MessageType.TALK)) {
            message.setMessage(message.getMessage());
            sendMessage(message,service);
        } else if (message.getType().equals(ChatDTO.MessageType.LEAVE)) {
            sessions.remove(session);
            message.setMessage(message.getSender() + " 님이 나갔습니다.");
            sendMessage(message, service);
        }
    }

    /** 채팅방에 입장하면 해당 채팅방의 소켓과 바로 연결*/
    public void enterAction(WebSocketSession session, ChatService service) {
        log.info("enterAction 메소드 호출");
        sessions.add(session);

        // 아래 두 줄은 테스트 완료되면 삭제하자
        // ChatService service 파라미터도 삭제 할 것
        String message = session.toString() + "님이 입장했습니다";
        sendMessage(message, service);
        log.info("enterAction 메소드 return");
    }

    public void leaveAction(WebSocketSession session, ChatService service) {
        log.info("leaveAction 메소드 호출");
        sessions.remove(session);

        // 아래 두 줄은 테스트 완료되면 삭제하자
        // ChatService service 파라미터도 삭제 할 것
        String message = session.toString() + "님이 퇴장했습니다";
        sendMessage(message, service);
        log.info("leaveAction 메소드 return");
    }

    public <T> void sendMessage(T message, ChatService service){
        // 병렬처리
        log.info("sessions = " + sessions.toString());
        sessions.parallelStream().forEach(sessions -> service.sendMessage(sessions,message));
    }
}
