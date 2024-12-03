package com.example.springsecurityexample.member.controller;

import com.example.springsecurityexample.member.Profile;
import com.example.springsecurityexample.member.dto.ProfileRequest;
import com.example.springsecurityexample.member.service.SignService;
import com.example.springsecurityexample.member.dto.SignRequest;
import com.example.springsecurityexample.member.dto.SignResponse;
import com.example.springsecurityexample.member.repository.MemberRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SignController {

    private final MemberRepository memberRepository;
    private final SignService memberService;

    @PostMapping(value = "/login")
    @ApiOperation("로그인")
    public ResponseEntity<SignResponse> login(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(memberService.login(request), HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    @ApiOperation("회원가입")
    public ResponseEntity<Boolean> signup(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(memberService.register(request), HttpStatus.OK);
    }

    @GetMapping("/user/get")
    @ApiOperation("유저의 정보 get")
    public ResponseEntity<SignResponse> getUser(@RequestParam String account) throws Exception {
        return new ResponseEntity<>( memberService.getMember(account), HttpStatus.OK);
    }

    @GetMapping("/admin/get")
    @ApiOperation("권한 정보 get")
    public ResponseEntity<SignResponse> getUserForAdmin(@RequestParam String account) throws Exception {
        return new ResponseEntity<>( memberService.getMember(account), HttpStatus.OK);
    }


    @PutMapping("/user/update/{id}")
    @ApiOperation("유저의 개인정보 업데이트")
    public ResponseEntity<String> updateMember(@PathVariable Long id,
                                               @RequestBody SignRequest signRequest) {
        log.info("@@@@@@@@@@@@@@@@@@@@@@@1됐다//////////////////////////////");
        boolean isUpdated = memberService.updateMemberInfo(id, signRequest);
        log.info("@@@@@@@@@@@@@@@@@@@@@@@2됐다//////////////////////////////");
        if (isUpdated) {
            return ResponseEntity.ok("회원 정보가 성공적으로 업데이트되었습니다.");

        } else {
            log.info("@@@@@@@@@@@@@@@@@@@@@@@됐다//////////////////////////////");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("업데이트할 정보가 없습니다.");
        }
    }
}