package com.example.springsecurityexample.projectProfileCard;

import com.example.springsecurityexample.projectProfileCard.dto.ProjectProfileCardRequestDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "projectId")
@Entity
public class ProjectProfileCard {
    @Id
    @GeneratedValue
    private Integer projectId;
    private Integer roomId;
    private String projectName;
    private String projectIntroduce;
    private LocalDateTime projectPeriod;
    private Boolean projectStatus;

    public void update(ProjectProfileCardRequestDto projectProfileCardRequestDto) {
        if (projectProfileCardRequestDto.getProjectName() != null) {
            this.projectName = projectProfileCardRequestDto.getProjectName();
        }
        if (projectProfileCardRequestDto.getProjectIntroduce() != null) {
            this.projectIntroduce = projectProfileCardRequestDto.getProjectIntroduce();
        }
        if (projectProfileCardRequestDto.getProjectPeriod() != null) {
            this.projectPeriod = projectProfileCardRequestDto.getProjectPeriod();
        }
        if (projectProfileCardRequestDto.getProjectStatus() != null) {
            this.projectStatus = projectProfileCardRequestDto.getProjectStatus();
        }
    }
}
