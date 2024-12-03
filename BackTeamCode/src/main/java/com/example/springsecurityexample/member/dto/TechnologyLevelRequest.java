package com.example.springsecurityexample.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnologyLevelRequest {
    private Long technologyId;
    private int level;

    public TechnologyLevelRequest(Long technologyId, int level) {
        this.technologyId = technologyId;
        this.level = level;
    }

    // 생성자, Getter, Setter 등

    public Long getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(Long technologyId) {
        this.technologyId = technologyId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}