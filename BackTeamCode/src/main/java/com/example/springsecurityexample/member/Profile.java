package com.example.springsecurityexample.member;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @Id
    private Long id;

    private String nickname;
    private String instruction;
    private String role;
    private boolean projectExperience;
    private String personalLink;
    private String photo;
    private int peer=50;

    @OneToOne
    @JoinColumn(name = "member_id") // Member 엔티티와의 관계를 매핑
    private Member member;

    public Profile(Member member) {
        this.member = member;
    }
}
