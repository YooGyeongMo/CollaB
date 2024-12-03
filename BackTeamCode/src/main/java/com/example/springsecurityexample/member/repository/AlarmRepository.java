package com.example.springsecurityexample.member.repository;

import com.example.springsecurityexample.member.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByReceiverUserId_Id(Long receiverUserId);
    List<Alarm> findBySenderUserId_Id(Long senderUserId);
    int countByReceiverUserId_Id(Long receiverUserId);

    int countByReceiverUserId_IdAndIsReadFalse(Long receiverUserId);

    List<Alarm> findByReceiverUserId_IdAndIsReadFalse (Long receiverUserId);
}
