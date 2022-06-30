package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.dto.ProfileDTO;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.service.MemberService;
import com.kr.realworldspringboot.service.ProfileService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final MemberService memberService;

    @GetMapping("/api/profiles/{username}")
    public JSONObject getProfile(@RequestAttribute Member member, @PathVariable String username){
        Member followMember = memberService.selectMemberByUsername(username);
        ProfileDTO profileDTO = profileService.findProfile(followMember.getId(),member.getId());
        return getReturnJsonObject(profileDTO);
    }

    @PostMapping("/api/profiles/{username}/follow")
    public JSONObject followUser(@RequestAttribute Member member, @PathVariable String username){
        profileService.followUser(member.getId(),username);

        Member followMember = memberService.selectMemberByUsername(username);
        ProfileDTO profileDTO = profileService.findProfile(followMember.getId(),member.getId());
        return getReturnJsonObject(profileDTO);
    }

    @DeleteMapping("/api/profiles/{username}/follow")
    public JSONObject unfollowUser(@RequestAttribute Member member, @PathVariable String username){
        profileService.unfollowUser(member.getId(), username);

        Member followMember = memberService.selectMemberByUsername(username);
        ProfileDTO profileDTO = profileService.findProfile(followMember.getId(),member.getId());
        return getReturnJsonObject(profileDTO);
    }

    public JSONObject getReturnJsonObject(ProfileDTO profileDTO){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("profile",profileDTO);
        return jsonObject;
    }

}
