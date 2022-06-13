package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.entity.Follow;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.service.MemberService;
import com.kr.realworldspringboot.service.ProfileService;
import com.kr.realworldspringboot.util.JWTUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @GetMapping("/api/profiles/{username}")
    public ResultProfile getProfile(@RequestHeader Map<String, Object> requestHeader, @PathVariable String username){
        boolean isFollow = false;

        if(requestHeader.get("authorization") != null){
            String loginEmail = jwtUtil.getEmailbyHeader(requestHeader.get("authorization").toString());
            Follow follow = Follow.builder()
                    .email(loginEmail)
                    .username(username)
                    .build();
            isFollow = profileService.isFollow(follow);
        }
        Member member = memberService.selectMemberByUsername(username);


        ResultProfileResponse profileRegisterResponse = ResultProfileResponse.builder()
                .username(member.getUsername())
                .bio(member.getBio())
                .image(member.getImage())
                .following(isFollow)
                .build();

        return new ResultProfile(profileRegisterResponse);
    }

    @PostMapping("/api/profiles/{username}/follow")
    public ResultProfile followUser(@RequestHeader Map<String, Object> requestHeader, @PathVariable String username){
        String loginEmail = jwtUtil.getEmailbyHeader(requestHeader.get("authorization").toString());

        Member member = memberService.selectMemberByUsername(username);

        Follow follow = Follow.builder()
                .email(loginEmail)
                .username(username)
                .build();

        profileService.followUser(follow);

        ResultProfileResponse memberRegisterResponse = ResultProfileResponse.builder()
                .username(member.getUsername())
                .bio(member.getBio())
                .image(member.getImage())
                .following(true)
                .build();

        return new ResultProfile(memberRegisterResponse);
    }

    @DeleteMapping("/api/profiles/{username}/follow")
    public ResultProfile unfollowUser(@RequestHeader Map<String, Object> requestHeader, @PathVariable String username){
        String loginEmail = jwtUtil.getEmailbyHeader(requestHeader.get("authorization").toString());

        Member member = memberService.selectMemberByUsername(username);

        Follow follow = Follow.builder()
                .email(loginEmail)
                .username(username)
                .build();

        profileService.unfollowUser(follow);

        ResultProfileResponse memberRegisterResponse = ResultProfileResponse.builder()
                .username(member.getUsername())
                .bio(member.getBio())
                .image(member.getImage())
                .following(false)
                .build();

        return new ResultProfile(memberRegisterResponse);
    }

    @Data
    @AllArgsConstructor
    static class ResultProfile {
        ProfileController.ResultProfileResponse profile;
    }

    @Data
    @AllArgsConstructor
    @Builder
    static class ResultProfileResponse {
        private String username;
        private String bio;
        private String image;
        private boolean following;
    }
}
