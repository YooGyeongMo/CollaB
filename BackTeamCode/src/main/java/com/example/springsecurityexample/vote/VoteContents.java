package com.example.springsecurityexample.vote;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteContents {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int voteContentsId;

    @Column (nullable = false)
    private int voteId;

    @Column (nullable = false)
    private String contents;

    @Column (nullable = false)
    private int voteNum;


}
