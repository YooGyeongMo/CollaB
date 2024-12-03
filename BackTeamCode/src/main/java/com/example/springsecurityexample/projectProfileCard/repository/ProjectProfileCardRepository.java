package com.example.springsecurityexample.projectProfileCard.repository;

import com.example.springsecurityexample.match.Match;
import com.example.springsecurityexample.projectProfileCard.ProjectProfileCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectProfileCardRepository extends JpaRepository<ProjectProfileCard, Integer> {
    List<ProjectProfileCard> findAllByRoomId(Integer roomId);
}
