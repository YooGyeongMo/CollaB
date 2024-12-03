package com.example.springsecurityexample.member.service;

import com.example.springsecurityexample.member.Alarm;
import com.example.springsecurityexample.member.dto.AlarmRequest;
import com.example.springsecurityexample.member.dto.AlarmResponse;
import com.example.springsecurityexample.member.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public AlarmResponse createAlarm(AlarmRequest alarmRequest) {
        Alarm alarm = new Alarm();
        alarm.setReceiverUserId(memberRepository.findById(alarmRequest.getReceiverUserId())
                .orElseThrow(() -> new EntityNotFoundException("Receiver User not found")));
        alarm.setSenderUserId(memberRepository.findById(alarmRequest.getSenderUserId())
                .orElseThrow(() -> new EntityNotFoundException("Sender User not found")));
        alarm.setMessage(alarmRequest.getMessage());
        alarm.setTimestamp(LocalDateTime.now());
        alarm.setRead(false);

        Alarm createdAlarm = alarmRepository.save(alarm);

        return convertToResponse(createdAlarm);
    }

    private AlarmResponse convertToResponse(Alarm alarm) {
        AlarmResponse alarmResponse = new AlarmResponse();
        alarmResponse.setId(alarm.getId());
        alarmResponse.setReceiverUserId(alarm.getReceiverUserId().getId());
        alarmResponse.setSenderUserId(alarm.getSenderUserId().getId());
        alarmResponse.setMessage(alarm.getMessage());
        alarmResponse.setTimestamp(alarm.getTimestamp());
        alarmResponse.setRead(alarm.isRead());
        return alarmResponse;
    }

    public List<AlarmResponse> getAlarmsForUser(Long userId){
        List<Alarm> alarms = alarmRepository.findByReceiverUserId_IdAndIsReadFalse(userId);
        return alarms.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAlarmAsRead(Long userId, Long alarmId) {
        // userId와 alarmId를 사용하여 알람을 찾음
        Optional<Alarm> optionalAlarm = alarmRepository.findById(alarmId);

        if (optionalAlarm.isPresent()) {
            Alarm alarm = optionalAlarm.get();

            // 알람을 읽음으로 표시
            alarm.setRead(true);

            // 변경 내용을 저장
            alarmRepository.save(alarm);
        } else {
            throw new EntityNotFoundException("Alarm not found");
        }
    }

    public int getAlarmNum(Long userId) {
        return alarmRepository.countByReceiverUserId_Id(userId);
    }
    public int getAlarmIsReadNum(Long userId) {
        return alarmRepository.countByReceiverUserId_IdAndIsReadFalse(userId);
    }
}