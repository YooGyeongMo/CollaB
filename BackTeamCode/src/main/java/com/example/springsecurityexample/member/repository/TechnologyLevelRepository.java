package com.example.springsecurityexample.member.repository;

import com.example.springsecurityexample.member.Profile;
import com.example.springsecurityexample.member.Technology;
import com.example.springsecurityexample.member.TechnologyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnologyLevelRepository extends JpaRepository<TechnologyLevel, Long> {
    // 추가적인 메서드가 필요한 경우 여기에 작성
    List<TechnologyLevel> findTechnologyLevelByMember(Member member);
    List<TechnologyLevel> findByMember_Account(String account);
    List<TechnologyLevel> findByProfile_Id(Long profileId);

    Optional<TechnologyLevel> findByProfileAndTechnology(Profile profile, Technology technology);

}
