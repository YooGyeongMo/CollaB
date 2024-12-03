package com.example.springsecurityexample.Chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Data
@Service
public class ChatService {
    private final ObjectMapper mapper;
    private Map<String, ChatRoom> chatRooms;
    private final JdbcTemplate jdbcTemplate;
    private Map<Object, ChatDTO> chatDTOs;


    @PostConstruct
    private void init(){
        chatRooms = new LinkedHashMap<>();
        chatDTOs = new LinkedHashMap<>();
    }

    // 새로운 채팅방이 만들어질때
    public ChatRoom createRoom(int user1, int user2) {
        String roomId = UUID.randomUUID().toString();
        String user1Name = new String();
        String user2Name = new String();
        // 최초의 채팅방 이름 설정
        String selectUserNameQuery = "select id, name from member where id in (?, ?)";
        Object[] userName = {user1, user2};
        List<Map<String, Object>> userNameList = jdbcTemplate.queryForList(selectUserNameQuery, userName);
        int count = 0;

        if(user1 > user2) {
            for (Map<String, Object> row : userNameList) {
                if(count == 0) {
                    user2Name = row.get("name").toString();
                }
                else {
                    user1Name = row.get("name").toString();
                    break;
                }
                count++;
            }
        }
        else {
            for (Map<String, Object> row : userNameList) {
                if(count == 0) {
                    user1Name = row.get("name").toString();
                }
                else {
                    user2Name = row.get("name").toString();
                    break;
                }
                count++;
            }
        }


        String roomName = new StringBuilder().append(user1Name).append(", ").append(user2Name).append("님의 채팅방").toString();
        LocalDateTime currTime = LocalDateTime.now();

        String insertUserQuery = "insert into chatroomparticipants values (?, ? ,?)";
        String insertChatRoomQuery = "insert into chatrooms values(?, ?, ?)";

        Object[] paramUser1 = {roomId, user1, currTime};
        Object[] paramUser2 = {roomId, user2, currTime};
        Object[] paramRoom = {roomId, roomName, currTime};

        jdbcTemplate.update(insertChatRoomQuery, paramRoom);
        jdbcTemplate.update(insertUserQuery, paramUser1);
        jdbcTemplate.update(insertUserQuery, paramUser2);

        //Builder를 사용하여 ChatRoom 을 Build
        ChatRoom room = ChatRoom.builder()
                .roomId(roomId)
                .name(roomName)
                .build();

        //랜덤 아이디와 room 정보를 Map 에 저장
        chatRooms.put(roomName, room);

        return room;
    }
    /** user2가 본인의 채팅방에 user1을 초대할때*/
    public ChatRoom createRoom(int user1, int user2, String session) {
        ChatRoom room = ChatRoom.builder()
                .roomId(session).name(session).build();
        LocalDateTime currTime = LocalDateTime.now();

        String insertUserQuery = "insert into chatroomparticipants values (?, ?, ?)";
        Object[] paramUser = {session, user1, currTime};
        jdbcTemplate.update(insertUserQuery, paramUser);
        return room;
    }
    /** user1이 본인의 채팅방에 user2를 초대할때*/
    public ChatRoom createRoom(int user1, String session, int user2) {
        ChatRoom room = ChatRoom.builder()
                .roomId(session).name(session).build();
        LocalDateTime currTime = LocalDateTime.now();

        String insertUserQuery = "insert into chatroomparticipants values (?, ?, ?)";
        Object[] paramUser = {session, user2, currTime};
        jdbcTemplate.update(insertUserQuery, paramUser);
        return room;
    }

    public List<ChatRoom> findRooms(int userId){
        // query로 내가 속한 채팅방들을 list에 저장
        String insertUserQuery = "select roomid, roomname from chatrooms where roomid in (select roomid from chatroomparticipants where userid = ?)";
        Object[] param = {userId};
        List<Map<String, Object>> results = jdbcTemplate.queryForList(insertUserQuery, param);

        // list에 담겨있는 채팅방들을 chatrooms에 roomid를 키로 저장
        chatRooms.clear();
        for (Map<String, Object> row : results) {
            String roomid = row.get("roomid").toString();
            String roomname = row.get("roomname").toString();
            ChatRoom chatRoom = new ChatRoom(roomid, roomname);
            chatRooms.put(roomid, chatRoom);
        }
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoom(String roomId){
        return chatRooms.get(roomId);
    }

    public void leaveRoom(String userId, String roomId) {
        String exitChatRoomQuery = "delete from chatroomparticipants where roomid = ? and userid = ?";
        Object[] param = {roomId, userId};
        jdbcTemplate.update(exitChatRoomQuery, param);
    }

    public void insertMessage(ChatDTO chatDTO, ChatRoom chatRoom) {

        String query2 = "insert into chatcontents(roomid, sender, message, senddate) values (?, ?, ?, ?)";
        Object[] param = {chatDTO.getRoomId(), chatDTO.getSender(), chatDTO.getMessage(), chatDTO.getSenddate()};
        log.info("query = [{}]", query2);
        jdbcTemplate.update(query2,param);
    }

    public List<ChatDTO> getMessages(String sendDate, String roomId) {
        String query2 = "select roomid, message, senddate, sender, name, sequence from chatcontents left join member on chatcontents.sender = member.id where roomid = ? and senddate LIKE ?";
        Object[] param = {roomId, sendDate + '%'};
        List<Map<String, Object>> results =  jdbcTemplate.queryForList(query2, param);

        chatDTOs.clear();
        for (Map<String, Object> row : results) {
            String roomid = row.get("roomid").toString();
            String message = row.get("message").toString();
            String senddate = row.get("senddate").toString();
            int sender = Integer.parseInt(row.get("sender").toString());
            String senderName = row.get("name").toString();
            Object sequence = row.get("sequence");

            ChatDTO chatDTO = new ChatDTO(ChatDTO.MessageType.TALK, roomid, sender, senderName, message, senddate);
            chatDTOs.put(sequence, chatDTO);
        }
        return new ArrayList<>(chatDTOs.values());
    }

    public <T> void sendMessage(WebSocketSession session, T message){
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        }catch (IOException e){
            log.error(e.getMessage(),e);
        }
    }

    public void changeRoomName(String roomId, String roomName) {
        String query= "update chatrooms set roomname = ? where roomid = ?";
        Object[] params = {roomName, roomId};
        jdbcTemplate.update(query, params);
    }
}

