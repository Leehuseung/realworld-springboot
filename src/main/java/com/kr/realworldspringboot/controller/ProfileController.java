package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.service.MemberService;
import com.kr.realworldspringboot.service.ProfileService;
import com.kr.realworldspringboot.util.JWTUtil;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

    @GetMapping("/api/profiles/{username}")
    public JSONObject getProfile(@RequestHeader Map<String, Object> requestHeader, @PathVariable String username){
        boolean isFollow = false;

        if(requestHeader.get("authorization") != null){
            String loginEmail = jwtUtil.getEmailbyHeader(requestHeader.get("authorization").toString());
            isFollow = profileService.isFollow(loginEmail, username);
        }
        Member member = memberService.selectMemberByUsername(username);

        ProfileResponse profileResponse = new ProfileResponse();
        modelMapper.map(member,profileResponse);
        profileResponse.setFollowing(isFollow);
        return getReturnJsonObject(profileResponse);
    }

    @PostMapping("/api/profiles/{username}/follow")
    public JSONObject followUser(@RequestHeader Map<String, Object> requestHeader, @PathVariable String username){
        String loginEmail = jwtUtil.getEmailbyHeader(requestHeader.get("authorization").toString());

        Member member = memberService.selectMemberByUsername(username);

        profileService.followUser(loginEmail,username);

        ProfileResponse profileResponse = new ProfileResponse();
        modelMapper.map(member,profileResponse);
        profileResponse.setFollowing(true);
        return getReturnJsonObject(profileResponse);
    }

    @DeleteMapping("/api/profiles/{username}/follow")
    public JSONObject unfollowUser(@RequestHeader Map<String, Object> requestHeader, @PathVariable String username){
        String loginEmail = jwtUtil.getEmailbyHeader(requestHeader.get("authorization").toString());

        Member member = memberService.selectMemberByUsername(username);

        profileService.unfollowUser(loginEmail, username);

        ProfileResponse profileResponse = new ProfileResponse();
        modelMapper.map(member,profileResponse);
        profileResponse.setFollowing(false);
        return getReturnJsonObject(profileResponse);
    }

    public JSONObject getReturnJsonObject(ProfileResponse profileResponse){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("profile",profileResponse);
        return jsonObject;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class ProfileResponse {
        private String username;
        private String bio;
        private String image;
        private boolean following;
    }
}
