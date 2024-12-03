package com.example.springsecurityexample.projectProfileCard.controller;

import com.example.springsecurityexample.projectProfileCard.ProjectProfileCard;
import com.example.springsecurityexample.projectProfileCard.dto.ProjectProfileCardRequestDto;
import com.example.springsecurityexample.projectProfileCard.repository.ProjectProfileCardRepository;
import com.example.springsecurityexample.projectProfileCard.service.ProjectProfileCardService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;


@Controller
@RequiredArgsConstructor //DI
@RequestMapping(value = "/api", produces = MediaTypes.HAL_JSON_VALUE)
public class ProjectProfileCardController {


    private final ProjectProfileCardService projectProfileCardService;
    private final ProjectProfileCardRepository projectProfileCardRepository;

    @ApiOperation(
            value = "프로젝트 프로필카드 생성"
            , notes = "프로젝트와 관련된 정보를 json으로 보내면 프로젝트 프로필카드를 생성한다." )
    @PostMapping("/post/project-profile-card")
    public ResponseEntity<ProjectProfileCard> CreateProjectProfileCard (@RequestBody ProjectProfileCardRequestDto projectProfileCardRequestDto) {
        try
        {
            return ResponseEntity.ok(projectProfileCardService.CreateProjectProfileCard(projectProfileCardRequestDto));
        }
        catch (Exception e)
        {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(null);
        }
    }

    @ApiOperation(
            value = "팀의 프로젝트 프로필카드 리스트 조회"
            , notes = "URI를 통해 받은 roomId로 팀에서 진행 중인 전체 프로젝트 프로필카드를 조회한다." )
    @GetMapping("/get/project-profile-card-list/{roomId}")
    public ResponseEntity<List<ProjectProfileCard>> ReadProjectProfileCardsInRoom (@PathVariable Integer roomId) {
        try
        {
            return ResponseEntity.ok(projectProfileCardService.GetProjectProfileCards(roomId));
        }
        catch(Exception e)
        {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(null);
        }
    }

    @ApiOperation(
            value = "팀의 프로젝트 프로필카드 1개 조회"
            , notes = "URI를 통해 받은 projectId로 한 개의 프로젝트 프로필카드를 조회한다." )
    @GetMapping("/get/project-profile-card/{projectId}")
    public ResponseEntity<ProjectProfileCard> ReadProjectProfileCard (@PathVariable Integer projectId) {
        try
        {
            return ResponseEntity.ok(projectProfileCardService.GetProjectProfileCard(projectId));
        }
        catch(Exception e)
        {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(null);
        }
    }

    @ApiOperation(
            value = "프로젝트 프로필카드 수정"
            , notes = "URI를 통해 받은 projectId로 프로젝트 프로필카드를 찾고 입력한 json파일을 통해 내용을 수정한다." )
    @PatchMapping("/patch/project-profile-card/{projectId}")
    public ResponseEntity<ProjectProfileCard> ReadProjectProfileCard
            (
                    @PathVariable Integer projectId,
                    @RequestBody ProjectProfileCardRequestDto projectProfileCardRequestDto
            )
    {
        try {
            return ResponseEntity.ok(
                    projectProfileCardService.GetProjectProfileCard(projectId, projectProfileCardRequestDto)
            );
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(null);
        }
    }

    @ApiOperation(
            value = "프로젝트 프로필카드 삭제"
            , notes = "URI를 통해 받은 projectId로 프로젝트 프로필카드를 삭제한다." )
    @DeleteMapping("/delete/project-profile-card/{projectId}")
    public ResponseEntity<String> DeleteProjectProfileCard (@PathVariable Integer projectId) {
        try {
            return ResponseEntity.ok(
                    projectProfileCardService.DeleteProjectProfileCard(projectId)
            );
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(null);
        }
    }
}
