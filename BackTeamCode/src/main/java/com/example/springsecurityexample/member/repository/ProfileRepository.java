package com.example.springsecurityexample.member.repository;

import com.example.springsecurityexample.member.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByMember(Member member);

    Optional<Profile> findByMember_Account(String userId);

    //매치를 위한 점수
    List<Profile> findByPeerBetween(int minValue, int maxValue);
    Optional<Profile> findByMember_Id(Long uid);

}
