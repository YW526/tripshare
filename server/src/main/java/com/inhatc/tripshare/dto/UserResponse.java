package com.inhatc.tripshare.dto;

public class UserResponse {
    private Long id;
    private String nickname;
    private String email;
    private String bio;

    public UserResponse(Long id, String nickname, String email, String bio) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }
}
