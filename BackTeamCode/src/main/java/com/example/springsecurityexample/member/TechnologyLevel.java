package com.example.springsecurityexample.member;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TechnologyLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "technology_id")
    private Technology technology;

    @ManyToOne
    @JoinColumn(name = "member_account")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;


    private int level;
}
