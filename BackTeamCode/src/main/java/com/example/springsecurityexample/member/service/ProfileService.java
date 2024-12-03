package com.example.springsecurityexample.member.service;

import com.example.springsecurityexample.member.Profile;
import com.example.springsecurityexample.member.dto.ProfileRequest;
import com.example.springsecurityexample.member.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;

    public void createProfile(Long memberId, Profile profile) {
        // 프로필을 생성할 때 회원과 관련된 정보를 연결합니다.
        profile.setMember(memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found")));
        profileRepository.save(profile);
    }
    public Profile UpdateProfile(Long memberId, ProfileRequest profileRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));


        Profile profile = profileRepository.findByMember(member)
                .orElse(new Profile());

        if (profileRequest.getNickname() != null) {
            profile.setNickname(profileRequest.getNickname());
        }
        if (profileRequest.getInstruction() != null) {
            profile.setInstruction(profileRequest.getInstruction());
        }
        if (profileRequest.getRole() != null) {
            profile.setRole(profileRequest.getRole());
        }
        if (profileRequest.getPersonalLink() !=null) {
            profile.setPersonalLink(profileRequest.getPersonalLink());
        }
        if(profileRequest.getPhoto() !=null){
            profile.setPhoto(profileRequest.getPhoto());
        }
        if( profileRequest.getPhoto() != null){
            profile.setPeer(profileRequest.getPeer());
        }
        if (profileRequest.isProjectExperience()){
            profile.setProjectExperience(true);
        }else {
            profile.setProjectExperience(false);
        }
        // 회원과 연결
        profile.setMember(member);

        return profileRepository.save(profile);


        /*
        // 프로필 정보 업데이트
        profile.setNickname(profileRequest.getNickname());
        profile.setInstruction(profileRequest.getInstruction());
        profile.setRole(profileRequest.getRole());
        profile.setProjectExperience(profileRequest.isProjectExperience());
        profile.setPersonalLink(profileRequest.getPersonalLink());
        profile.setPhoto(profileRequest.getPhoto());
        profile.setMember(member);

        return profileRepository.save(profile);
        */
    }

    public Profile getProfileByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        return profileRepository.findByMember(member)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
    }
}