package com.example.springsecurityexample.member.controller;

import com.example.springsecurityexample.member.TechnologyLevel;
import com.example.springsecurityexample.member.dto.TechnologyLevelRequest;
import com.example.springsecurityexample.member.dto.TechnologyLevelUpdateRequest;
import com.example.springsecurityexample.member.service.TechnologyLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/technology-levels")
public class TechnologyLevelController {

    private final TechnologyLevelService technologyLevelService;

    @Autowired
    public TechnologyLevelController(TechnologyLevelService technologyLevelService) {
        this.technologyLevelService = technologyLevelService;
    }
    // 기술 스택 레벨 추가
    @PostMapping("/member/{userId}")
    @ApiOperation(
            value = "account에 따른 기술 스택 레벨 추가"
            , notes = "technologyId는 TechnologyController에서 추가된 기술 id, level은 현재레벨")
    public ResponseEntity<String> addTechnologyLevel(
            @PathVariable String userId,
            @RequestBody TechnologyLevelRequest requestDTO
    ) {
        technologyLevelService.addTechnologyLevel(userId, requestDTO);
        return new ResponseEntity<>("Technology level added successfully", HttpStatus.CREATED);
    }
    @PostMapping("/profile/{profileId}")
    @ApiOperation(
            value = "profileId에 따른 기술 스택 레벨 추가"
            , notes = "technologyId는 TechnologyController에서 추가된 기술 id, level은 현재레벨")
    public ResponseEntity<String> addTechnologyLevelProfile(
            @PathVariable Long profileId,
            @RequestBody TechnologyLevelRequest requestDTO
    ) {
        technologyLevelService.addTechnologyLevelProfile(profileId, requestDTO);
        return new ResponseEntity<>("Technology level added successfully", HttpStatus.CREATED);
    }



    // TechnologyLevel 저장
    @PostMapping
    @ApiOperation("테이블에 수동저장 (웬만하면 사용x)")
    public ResponseEntity<String> saveTechnologyLevel(@RequestBody TechnologyLevel technologyLevel) {
        technologyLevelService.saveTechnologyLevel(technologyLevel);
        return new ResponseEntity<>("TechnologyLevel이 성공적으로 저장되었습니다.", HttpStatus.CREATED);
    }

    // 모든 TechnologyLevel 조회
    @GetMapping
    @ApiOperation("기술스택 테이블에 저장되어있는 모든 목록 조회")
    public ResponseEntity<List<TechnologyLevel>> getAllTechnologyLevels() {
        List<TechnologyLevel> technologyLevels = technologyLevelService.getAllTechnologyLevels();
        return new ResponseEntity<>(technologyLevels, HttpStatus.OK);
    }

    // 특정 Profile에 속한 TechnologyLevel 조회
    @GetMapping("/member/{account}")
    @ApiOperation(value = "account에 따른 기술 스택 레벨 조회")
    public ResponseEntity<List<TechnologyLevel>> getTechnologyLevelsByAccountId(@PathVariable String account) {
        List<TechnologyLevel> technologyLevels = technologyLevelService.getTechnologyLevelsByAccountId(account);
        return new ResponseEntity<>(technologyLevels, HttpStatus.OK);
    }

    @GetMapping("/profile/{profileId}")
    @ApiOperation(value = "profileId 따른 기술 스택 레벨 조회")
    public ResponseEntity<List<TechnologyLevel>> getTechnologyLevelsByProfileId(@PathVariable Long profileId) {
        List<TechnologyLevel> technologyLevels = technologyLevelService.getTechnologyLevelsByProfileId(profileId);
        return new ResponseEntity<>(technologyLevels, HttpStatus.OK);
    }

    @PutMapping("/profile/{profileId}/technology/{technologyId}")
    @ApiOperation("특정 Profile의 특정 기술 스택 레벨 변경")
    public ResponseEntity<String> updateTechnologyLevel(
            @PathVariable Long profileId,
            @PathVariable Long technologyId,
            @RequestBody TechnologyLevelUpdateRequest request) {
        technologyLevelService.updateTechnologyLevel(profileId, technologyId, request.getLevel());
        return new ResponseEntity<>("Technology level updated successfully", HttpStatus.OK);
    }
}