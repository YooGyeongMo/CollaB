package com.example.springsecurityexample.vote.repository;

import com.example.springsecurityexample.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findVotesByRoomId(String roomId);
    Vote findVotesByVoteId(int voteId);
}
