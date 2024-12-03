package com.example.springsecurityexample.vote.controller;

import com.example.springsecurityexample.vote.Vote;
import com.example.springsecurityexample.vote.dto.VoteDTO;
import com.example.springsecurityexample.vote.service.VoteService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/vote")
public class VoteController {

    private final VoteService voteService;


    @GetMapping("/all/{roomId}")
    @ApiOperation("roomId에 해당하는 모든 투표를 조회")
    public List<Vote> getAllVotes(@PathVariable("roomId") String roomId) {
        return voteService.getAllVotes(roomId);
    }

    @PostMapping("/new")
    @ApiOperation("투표 생성")
    public ResponseEntity<VoteDTO> createVote(@RequestBody VoteDTO voteDTO) {
        VoteDTO createdVote = voteService.createVote(voteDTO);
        return new ResponseEntity<>(createdVote, HttpStatus.CREATED);
    }

    @PatchMapping("/end/{voteId}")
    @ApiOperation("투표종료")
    public boolean endVote(@PathVariable int voteId) {
        return voteService.endVote(voteId);
    }
}
