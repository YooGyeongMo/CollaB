package com.example.springsecurityexample.member.dto;

import com.example.springsecurityexample.member.Authority;
import com.example.springsecurityexample.member.Member;
import com.example.springsecurityexample.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Builder @AllArgsConstructor @NoArgsConstructor
public class SignResponse {
    private Long id;

    private String account;


    private String name;

    private String email;

    private Date birthday;

    private String phoneNumber;

    private String gender;

    private String major;

    private List<Authority> roles = new ArrayList<>();

    private String token;

    public SignResponse(Member member) {
        this.id = member.getId();
        this.account = member.getAccount();
        this.name = member.getName();
        this.email = member.getEmail();
        this.roles = member.getRoles();
        this.birthday = member.getBirthday();
        this.phoneNumber=member.getPhoneNumber();
        this.gender = member.getGender();
        this.major=member.getMajor();
        this.token=member.getToken();
    }
}
