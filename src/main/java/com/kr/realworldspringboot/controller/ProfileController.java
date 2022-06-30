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
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

    @GetMapping("/api/profiles/{username}")
    public JSONObject getProfile(@RequestAttribute Member member, @PathVariable String username){
        boolean isFollow = false;

        if(member.isValid()){
            isFollow = profileService.isFollow(member.getEmail(), username);
        }
        Member profileMember = memberService.selectMemberByUsername(username);

        ProfileResponse profileResponse = new ProfileResponse();
        modelMapper.map(profileMember,profileResponse);
        profileResponse.setFollowing(isFollow);
        return getReturnJsonObject(profileResponse);
    }

    @PostMapping("/api/profiles/{username}/follow")
    public JSONObject followUser(@RequestAttribute Member member, @PathVariable String username){
        profileService.followUser(member.getEmail(),username);

        Member followMember = memberService.selectMemberByUsername(username);

        ProfileResponse profileResponse = new ProfileResponse();
        modelMapper.map(followMember,profileResponse);
        profileResponse.setFollowing(true);
        return getReturnJsonObject(profileResponse);
    }

    @DeleteMapping("/api/profiles/{username}/follow")
    public JSONObject unfollowUser(@RequestAttribute Member member, @PathVariable String username){
        profileService.unfollowUser(member.getEmail(), username);

        Member followMember = memberService.selectMemberByUsername(username);

        ProfileResponse profileResponse = new ProfileResponse();
        modelMapper.map(followMember,profileResponse);
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
