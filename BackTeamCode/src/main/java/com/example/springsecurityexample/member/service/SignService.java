package com.example.springsecurityexample.member.service;

import com.example.springsecurityexample.member.Authority;
import com.example.springsecurityexample.member.Member;
import com.example.springsecurityexample.member.Profile;
import com.example.springsecurityexample.member.dto.ProfileRequest;
import com.example.springsecurityexample.member.dto.SignRequest;
import com.example.springsecurityexample.member.dto.SignResponse;
import com.example.springsecurityexample.member.repository.MemberRepository;
import com.example.springsecurityexample.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class SignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final ProfileService profileService;



    public SignResponse login(SignRequest request) throws Exception {
        Member member = memberRepository.findByAccount(request.getAccount()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }
        String token = jwtProvider.createToken(member.getAccount(), member.getRoles());
        // Member 객체에 토큰 저장
        member.setToken(token);

        return SignResponse.builder()
                .id(member.getId())
                .account(member.getAccount())
                .name(member.getName())
                .email(member.getEmail())
                .birthday(member.getBirthday())
                .phoneNumber(member.getPhoneNumber())
                .gender(member.getGender())
                .major(member.getMajor())
                .roles(member.getRoles())
                .token(token)
                .build();
    }
    @Transactional
    public boolean register(SignRequest request) throws Exception {
        try {
            Member member = Member.builder()
                    .account(request.getAccount())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .email(request.getEmail())
                    .birthday(request.getBirthday())
                    .phoneNumber(request.getPhoneNumber())
                    .gender(request.getGender())
                    .major(request.getMajor())
                    .build();

            member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

            memberRepository.save(member);

            // 프로필 생성
            Profile profile = Profile.builder()
                    .id(member.getId())
                    .member(member)
                    .build();
            profileService.createProfile(member.getId(), profile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    public SignResponse getMember(String account) throws Exception {
        Member member = memberRepository.findByAccount(account)
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
        return new SignResponse(member);
    }


    public boolean updateMemberInfo(Long id, SignRequest signRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        boolean isUpdated = false;
        if (signRequest.getEmail() != null && !signRequest.getEmail().isEmpty()) {
            member.setEmail(signRequest.getEmail());
            isUpdated = true;
        }
        if (signRequest.getPhoneNumber() != null && !signRequest.getPhoneNumber().isEmpty()) {
            member.setPhoneNumber(signRequest.getPhoneNumber());
            isUpdated = true;
        }
        if (signRequest.getMajor() != null && !signRequest.getMajor().isEmpty()) {
            member.setMajor(signRequest.getMajor());
            isUpdated = true;
        }

        if (isUpdated) {
            memberRepository.save(member);
        }

        return isUpdated;
    }
}
