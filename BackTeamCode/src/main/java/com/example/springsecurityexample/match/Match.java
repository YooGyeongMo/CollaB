package com.example.springsecurityexample.match;

import com.example.springsecurityexample.member.Profile;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter @EqualsAndHashCode(of = "matchId")
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"uid1", "uid2"})
})
public class Match {
    @Id @GeneratedValue
    private Long matchId;
    private Long uid1;// replace uid
    private Boolean isUid2Team;
    private LocalDateTime matchingDate;
    @Enumerated(EnumType.STRING)
    private MatchRequestType matchingRequest = MatchRequestType.PENDING;
    @Enumerated(EnumType.STRING)
    private MatchSuccessType matchSuccess = MatchSuccessType.PENDING;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "uid2")
    private Profile profile;


    //TODO : update() - isUid2Team field
    public void update(){
        if(false) // team table 생성되면 조건문 변경
            this.isUid2Team = true;
        else
            this.isUid2Team = false;
    }

}
