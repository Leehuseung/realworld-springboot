package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.ProfileDTO;

public interface ProfileService {

    boolean isFollow(String loginEmail, String username);

    void followUser(Long memberId, String username);

    void unfollowUser(Long memberId, String username);

    ProfileDTO findProfile(Long memberId, Long loginMemberId);
}
