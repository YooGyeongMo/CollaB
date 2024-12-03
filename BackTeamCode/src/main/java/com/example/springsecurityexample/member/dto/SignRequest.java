package com.example.springsecurityexample.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class SignRequest {
    private Long id;

    private String account;

    private String password;

    private String name;

    private String email;

    private Date birthday;

    private String phoneNumber;

    private String gender;

    private String major;
}

