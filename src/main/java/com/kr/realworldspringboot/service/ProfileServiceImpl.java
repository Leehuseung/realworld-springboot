package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.ProfileDTO;
import com.kr.realworldspringboot.entity.Follow;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.exception.DuplicateException;
import com.kr.realworldspringboot.repository.MemberRepository;
import com.kr.realworldspringboot.repository.ProfileQueryRepository;
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
    private final ProfileQueryRepository profileQueryRepository;

    @Override
    public boolean isFollow(String userEmail, String username) {
        if(userEmail == null){
            return false;
        }
        Member member = memberRepository.findByEmail(userEmail).get();
        Member followMember = memberRepository.findMemberByUsername(username);
        Optional<Follow> optional = profileRepository.findByMemberIdAndFollowUsername(member.getId(), followMember.getId());
        return optional.isEmpty() ? false : true;
    }

    @Override
    public void followUser(Long memberId, String username) {
        Member member = memberRepository.findById(memberId).get();
        Member followMember = memberRepository.findMemberByUsername(username);

        Optional<Follow> optional = profileRepository.findByMemberIdAndFollowUsername(member.getId(), followMember.getId());
        if(!optional.isEmpty()){
            throw new DuplicateException("username");
        }

        Follow follow = Follow.builder()
                .memberId(member.getId())
                .followMemberId(followMember.getId())
                .build();
        profileRepository.save(follow);
    }

    @Override
    public void unfollowUser(Long memberId, String username) {
        Member member = memberRepository.findById(memberId).get();
        Member followMember = memberRepository.findMemberByUsername(username);

        Follow selectFollow = profileRepository.findByMemberIdAndFollowUsername(member.getId(), followMember.getId()).get();
        profileRepository.delete(selectFollow);
    }

    @Override
    public ProfileDTO findProfile(Long memberId, Long loginMemberId) {
        if(loginMemberId == null){
            loginMemberId = -1l;
        }
        ProfileDTO profileDTO = profileQueryRepository.getProfile(memberId,loginMemberId);

        return profileDTO;
    }


}
