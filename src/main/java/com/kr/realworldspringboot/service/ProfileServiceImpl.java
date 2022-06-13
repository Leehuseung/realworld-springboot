package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.entity.Follow;
import com.kr.realworldspringboot.exception.DuplicateException;
import com.kr.realworldspringboot.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;

    @Override
    public boolean isFollow(Follow follow) {
        Optional<Follow> optional = profileRepository.findByEmailAndFollowUsername(follow.getEmail(), follow.getUsername());
        return optional.isEmpty() ? false : true;
    }

    @Override
    public void followUser(Follow follow) {
        Optional<Follow> optional = profileRepository.findByEmailAndFollowUsername(follow.getEmail(), follow.getUsername());
        if(!optional.isEmpty()){
            throw new DuplicateException("username");
        }

        profileRepository.save(follow);
    }

    @Override
    public void unfollowUser(Follow follow) {
        Follow selectFollow = profileRepository.findByEmailAndFollowUsername(follow.getEmail(),follow.getUsername()).get();
        profileRepository.delete(selectFollow);
    }


}
