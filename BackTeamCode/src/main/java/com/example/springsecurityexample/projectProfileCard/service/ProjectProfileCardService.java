package com.example.springsecurityexample.projectProfileCard.service;

import com.example.springsecurityexample.projectProfileCard.ProjectProfileCard;
import com.example.springsecurityexample.projectProfileCard.dto.ProjectProfileCardRequestDto;
import com.example.springsecurityexample.projectProfileCard.repository.ProjectProfileCardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional
@RequiredArgsConstructor
public class ProjectProfileCardService {
    private final ProjectProfileCardRepository projectProfileCardRepository;
    private final ModelMapper modelMapper;

    public ProjectProfileCard CreateProjectProfileCard (ProjectProfileCardRequestDto projectProfileCardInfo){
        ProjectProfileCard card = modelMapper.map(projectProfileCardInfo, ProjectProfileCard.class);
        return projectProfileCardRepository.save(card);
    }

    public List<ProjectProfileCard> GetProjectProfileCards (Integer roomId){
        List<ProjectProfileCard> cards = projectProfileCardRepository.findAllByRoomId(roomId);
        if (cards.isEmpty()) {
            throw new NoSuchElementException("해당 방 ID에 해당하는 프로젝트 프로필카드가 존재하지 않습니다.");
        }
        return cards;
    }

    public ProjectProfileCard GetProjectProfileCard (Integer projectId){
        return projectProfileCardRepository.findById(projectId)
                .orElseThrow(() -> new NullPointerException("해당 프로젝트 프로필카드가 존재하지 않습니다."));
    }

    public ProjectProfileCard GetProjectProfileCard(
            Integer projectId,
            ProjectProfileCardRequestDto projectProfileCardRequestDto
    )
    {
        ProjectProfileCard card = projectProfileCardRepository.findById(projectId)
                .orElseThrow(() -> new NullPointerException("해당 아이디가 존재하지 않습니다."));
        card.update(projectProfileCardRequestDto);
        projectProfileCardRepository.save(card);
        return card;
    }

    public String DeleteProjectProfileCard (@PathVariable Integer projectId) {
        ProjectProfileCard card = projectProfileCardRepository.findById(projectId)
                .orElseThrow(() -> new NullPointerException("해당 아이디가 존재하지 않습니다."));
        projectProfileCardRepository.delete(card);
        return "성공했습니다";
    }
}
