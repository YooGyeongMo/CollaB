package com.example.springsecurityexample.match.repository;

import com.example.springsecurityexample.match.Match;
import com.example.springsecurityexample.match.MatchRequestType;
import com.example.springsecurityexample.match.MatchSuccessType;
import com.example.springsecurityexample.member.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

//@Primary

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findAllByUid1AndMatchingRequestIsFalse(Long Uid1);
    List<Match> findAllByUid1(Long Uid1);
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Match> findByUid1AndProfile_Id(Long Uid1, Long Uid2);
    Optional<Match> findByUid1(Long Uid1);
    void deleteByMatchingRequest(MatchRequestType matchingRequest);
    void deleteByMatchSuccess(MatchSuccessType matchSuccessType);

    //    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Match> findByUid1AndProfile(Long uid1, Profile profile);
}