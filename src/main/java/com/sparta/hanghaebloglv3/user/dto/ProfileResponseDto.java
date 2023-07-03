package com.sparta.hanghaebloglv3.user.dto;

import lombok.Getter;

@Getter
public class ProfileResponseDto {
	private String username;
	private String introduction;

    public ProfileResponseDto(String username, String introduction) {
        this.username = username;
        this.introduction = introduction;
    }
}