package com.example.springsecurityexample.projectProfileCard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectProfileCardRequestDto {
    private Integer roomId;
    private String projectName;
    private String projectIntroduce;
    private LocalDateTime projectPeriod;
    private Boolean projectStatus;
}
