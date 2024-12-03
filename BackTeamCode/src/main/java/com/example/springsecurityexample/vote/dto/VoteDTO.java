package com.example.springsecurityexample.vote.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Builder
@Getter @Setter
public class VoteDTO {
    private int voteId;
    private String roomId;
    private String voteTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean voteStatus;
    private int totalVoteNumber;
}
