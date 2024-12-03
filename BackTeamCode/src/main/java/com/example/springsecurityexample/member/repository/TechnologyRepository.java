package com.example.springsecurityexample.member.repository;

import com.example.springsecurityexample.member.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    // 추가적인 메서드가 필요한 경우 여기에 작성
}
