package com.example.springsecurityexample.member.service;

import com.example.springsecurityexample.member.Technology;
import com.example.springsecurityexample.member.dto.TechnologyRequest;
import com.example.springsecurityexample.member.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    @Autowired
    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    // 기술 스택 추가
    public void addTechnology(TechnologyRequest technologyRequest) {
        Technology technology = new Technology();
        technology.setName(technologyRequest.getName());
        technologyRepository.save(technology);
    }

    // 모든 기술 스택 조회
    public List<Technology> getAllTechnologies() {
        return technologyRepository.findAll();
    }
}
