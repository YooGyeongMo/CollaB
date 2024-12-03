package com.example.springsecurityexample.member.controller;

import com.example.springsecurityexample.member.dto.AlarmRequest;
import com.example.springsecurityexample.member.dto.AlarmResponse;
import com.example.springsecurityexample.member.service.AlarmService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;

    @PostMapping("/alarms")
    @ApiOperation("알람 생성")
    public ResponseEntity<AlarmResponse> createAlarm(@RequestBody AlarmRequest alarmRequest) {
        AlarmResponse createdAlarm = alarmService.createAlarm(alarmRequest);
        return new ResponseEntity<>(createdAlarm, HttpStatus.CREATED);
    }

    // 사용자의 알람목록 조회
    @GetMapping("/alarms/{responseId}")
    @ApiOperation("사용자의 알림목록을 조회. responseId는 알람을 받는 사람.")
    public ResponseEntity<List<AlarmResponse>> getAlarmsForUser(@PathVariable Long responseId) {
        List<AlarmResponse> alarms = alarmService.getAlarmsForUser(responseId);
        return new ResponseEntity<>(alarms, HttpStatus.OK);
    }

    // 알람 읽음표시
    // userid까지 함께 매핑
    @PutMapping("alarms/{userId}/{alarmId}/read")
    @ApiOperation("사용자의 알람목록 중 하나를 읽음으로 설정")
    public ResponseEntity<String> markAlarmAsRead(@PathVariable Long userId, @PathVariable Long alarmId) {
        try {
            alarmService.markAlarmAsRead(userId, alarmId);
            return new ResponseEntity<>("Alarm marked as read", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("alarms/num/{userId}")
    @ApiOperation("사용자의 전체 알림 목록의 갯수를 반환")
    public ResponseEntity<Integer> getAlarmsNum(@PathVariable Long userId) {
        int numAlarms = alarmService.getAlarmNum(userId);
        return new ResponseEntity<>(numAlarms, HttpStatus.OK);
    }

    @GetMapping("alarms/readnum/{userId}")
    @ApiOperation("사용자의 전체 알림 목록의 갯수를 반환")
    public ResponseEntity<Integer> getAlarmsReadNum(@PathVariable Long userId) {
        int numAlarms = alarmService.getAlarmIsReadNum(userId);
        return new ResponseEntity<>(numAlarms, HttpStatus.OK);
    }

}
