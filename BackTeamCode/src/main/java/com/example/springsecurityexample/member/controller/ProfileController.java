package com.example.springsecurityexample.member.controller;

import com.example.springsecurityexample.member.Profile;
import com.example.springsecurityexample.member.dto.ProfileRequest;
import com.example.springsecurityexample.member.service.ProfileService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/profile/{memberId}")
    @ApiOperation("memberId에 해당하는 프로필 정보를 저장")
    public ResponseEntity<Profile> createOrUpdateProfile(@PathVariable Long memberId, @RequestBody ProfileRequest profileRequest) {
        Profile profile = profileService.UpdateProfile(memberId, profileRequest);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @GetMapping("/profile/{memberId}")
    @ApiOperation("memberId의 프로필 정보 가져옴")
    public ResponseEntity<Profile> getProfileByMemberId(@PathVariable Long memberId) {
        Profile profile = profileService.getProfileByMemberId(memberId);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }


}