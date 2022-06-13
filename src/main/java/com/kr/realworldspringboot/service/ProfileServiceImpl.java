package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.entity.Follow;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.exception.DuplicateException;
import com.kr.realworldspringboot.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Override
    public boolean isFollow(String userEmail, String username) {
        Member member = memberRepository.findMemberByUsername(username);
        Optional<Follow> optional = profileRepository.findByMemberId(member.getId());
        return optional.isEmpty() ? false : true;
    }

    @Override
    public void followUser(String loginEmail, String username) {
        Member member = memberRepository.findMemberByUsername(username);

        Optional<Follow> optional = profileRepository.findByMemberIdAndFollowUsername(member.getId(), username);
        if(!optional.isEmpty()){
            throw new DuplicateException("username");
        }
        Follow follow = Follow.builder()
                .memberId(member.getId())
                .username(username)
                .build();
        profileRepository.save(follow);
    }

    @Override
    //TODO unfollow refacotiring
    public void unfollowUser(String loginEmail, String username) {
        Member member = memberRepository.findMemberByUsername(username);

        Follow selectFollow = profileRepository.findByMemberIdAndFollowUsername(member.getId(), username).get();
        profileRepository.delete(selectFollow);
    }


}
