package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.entity.Follow;

public interface ProfileService {

    boolean isFollow(Follow follow);

    void followUser(Follow follow);

    void unfollowUser(Follow follow);
}
