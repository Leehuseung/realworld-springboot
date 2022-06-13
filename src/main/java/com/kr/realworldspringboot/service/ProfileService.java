package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.entity.Follow;

public interface ProfileService {

    boolean isFollow(String loginEmail, String username);

    void followUser(String loginEmail, String username);

    void unfollowUser(String loginEmail, String username);
}
