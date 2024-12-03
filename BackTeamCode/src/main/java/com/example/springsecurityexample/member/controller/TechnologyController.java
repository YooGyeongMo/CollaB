package com.example.springsecurityexample.member.controller;

import com.example.springsecurityexample.member.Technology;
import com.example.springsecurityexample.member.dto.TechnologyRequest;
import com.example.springsecurityexample.member.service.TechnologyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/technologies")
public class TechnologyController {

    private final TechnologyService technologyService;

    @Autowired
    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    // 기술 스택 추가
    @PostMapping
    @ApiOperation("기술 스택 추가")
    public ResponseEntity<String> addTechnology(@RequestBody TechnologyRequest technologyRequest) {
        technologyService.addTechnology(technologyRequest);
        return new ResponseEntity<>("Technology added successfully", HttpStatus.CREATED);
    }

    // 모든 기술 스택 조회
    @GetMapping
    @ApiOperation("모든 기술 스택 종류 조회")
    public ResponseEntity<List<Technology>> getAllTechnologies() {
        List<Technology> technologies = technologyService.getAllTechnologies();
        return new ResponseEntity<>(technologies, HttpStatus.OK);
    }
}
