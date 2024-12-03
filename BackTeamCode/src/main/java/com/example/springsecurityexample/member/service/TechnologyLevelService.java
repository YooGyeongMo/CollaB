package com.example.springsecurityexample.member.service;

import com.example.springsecurityexample.member.Profile;
import com.example.springsecurityexample.member.Technology;
import com.example.springsecurityexample.member.TechnologyLevel;
import com.example.springsecurityexample.member.dto.TechnologyLevelRequest;
import com.example.springsecurityexample.member.repository.ProfileRepository;
import com.example.springsecurityexample.member.repository.TechnologyLevelRepository;
import com.example.springsecurityexample.member.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TechnologyLevelService {

    private final TechnologyLevelRepository technologyLevelRepository;
    private final TechnologyRepository technologyRepository;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public TechnologyLevelService(
            TechnologyLevelRepository technologyLevelRepository,
            TechnologyRepository technologyRepository,
            MemberRepository memberRepository,
            ProfileRepository profileRepository) {
        this.technologyLevelRepository = technologyLevelRepository;
        this.technologyRepository = technologyRepository;
        this.memberRepository = memberRepository;
        this.profileRepository = profileRepository;
    }

    public void addTechnologyLevel(String userId, TechnologyLevelRequest requestDTO) {
        // 프로필을 찾기 위해 사용자 ID로 프로필 엔터티 가져오기
        Member member = memberRepository.findByAccount(userId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Profile profile = profileRepository.findByMember_Account(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        // technologyId로 기술 스택 엔터티 가져오기
        Technology technology = technologyRepository.findById(requestDTO.getTechnologyId())
                .orElseThrow(() -> new EntityNotFoundException("Technology not found"));

        // 기술 스택 레벨 엔터티 생성 및 설정
        TechnologyLevel technologyLevel = new TechnologyLevel();
        technologyLevel.setTechnology(technology);
        technologyLevel.setMember(member);
        technologyLevel.setProfile(profile);
        technologyLevel.setLevel(requestDTO.getLevel());

        // 기술 스택 레벨 저장
        technologyLevelRepository.save(technologyLevel);
    }

    public void addTechnologyLevelProfile(Long userId, TechnologyLevelRequest requestDTO) {
        // 프로필을 찾기 위해 사용자 ID로 프로필 엔터티 가져오기
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        // technologyId로 기술 스택 엔터티 가져오기
        Technology technology = technologyRepository.findById(requestDTO.getTechnologyId())
                .orElseThrow(() -> new EntityNotFoundException("Technology not found"));

        // 기술 스택 레벨 엔터티 생성 및 설정
        TechnologyLevel technologyLevel = new TechnologyLevel();
        technologyLevel.setTechnology(technology);
        technologyLevel.setMember(member);
        technologyLevel.setProfile(profile);
        technologyLevel.setLevel(requestDTO.getLevel());

        // 기술 스택 레벨 저장
        technologyLevelRepository.save(technologyLevel);
    }



    // TechnologyLevel 저장
    public void saveTechnologyLevel(TechnologyLevel technologyLevel) {
        technologyLevelRepository.save(technologyLevel);
    }

    // 모든 TechnologyLevel 조회
    public List<TechnologyLevel> getAllTechnologyLevels() {
        return technologyLevelRepository.findAll();
    }

    // 특정 Profile에 속한 TechnologyLevel 조회
    public List<TechnologyLevel> getTechnologyLevelsByAccountId(String account) {
        // 여기에 적절한 로직을 추가하여 특정 Profile에 속한 TechnologyLevel을 조회
        // 예를 들면, technologyLevelRepository.findByProfileId(profileId) 등의 메서드 사용
        return technologyLevelRepository.findByMember_Account(account);
    }

    public List<TechnologyLevel> getTechnologyLevelsByProfileId(Long profileId) {
        // 여기에 적절한 로직을 추가하여 특정 Profile에 속한 TechnologyLevel을 조회
        // 예를 들면, technologyLevelRepository.findByProfileId(profileId) 등의 메서드 사용
        return technologyLevelRepository.findByProfile_Id(profileId);
    }

    public void updateTechnologyLevel(Long profileId, Long technologyId, int newLevel) {
        // Profile과 Technology를 찾고, 해당하는 TechnologyLevel을 조회합니다.
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
        Technology technology = technologyRepository.findById(technologyId)
                .orElseThrow(() -> new EntityNotFoundException("Technology not found"));

        TechnologyLevel technologyLevel = technologyLevelRepository.findByProfileAndTechnology(profile, technology)
                .orElseThrow(() -> new EntityNotFoundException("TechnologyLevel not found"));

        // 새로운 레벨로 설정합니다.
        technologyLevel.setLevel(newLevel);

        // 업데이트된 객체를 저장합니다.
        technologyLevelRepository.save(technologyLevel);
    }

}
