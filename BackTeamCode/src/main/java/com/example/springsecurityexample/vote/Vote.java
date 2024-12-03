package com.example.springsecurityexample.vote;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Vote {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int voteId;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private String voteTitle;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    @Setter
    private boolean voteStatus;

    @Column(nullable = false)
    private int totalVoteNumber;

}
