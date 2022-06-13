package com.kr.realworldspringboot.service;

public interface ProfileService {

    boolean isFollow(String loginEmail, String username);

    void followUser(String loginEmail, String username);

    void unfollowUser(String loginEmail, String username);
}
