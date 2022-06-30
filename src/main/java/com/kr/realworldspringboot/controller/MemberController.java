package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.dto.MemberRegisterDTO;
import com.kr.realworldspringboot.dto.MemberUpdateDTO;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.service.MemberService;
import com.kr.realworldspringboot.util.JWTUtil;
import lombok.*;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

    @PostMapping("/api/users")
    public JSONObject registerMember(@RequestBody @Valid MemberRegisterDTO memberRegisterDTO){
        Long id = memberService.registerMember(memberRegisterDTO);
        Member member = memberService.selectMemberById(id);

        MemberResponse memberResponse = new MemberResponse();
        modelMapper.map(member,memberResponse);
        return getReturnJsonObject(memberResponse);
    }

    @GetMapping("/api/user")
    public JSONObject getMember(@RequestAttribute Member member, @RequestAttribute String token) {
        MemberResponse memberResponse = new MemberResponse();
        modelMapper.map(member,memberResponse);
        memberResponse.setToken(token);
        return getReturnJsonObject(memberResponse);
    }

    @PutMapping("/api/user")
    public JSONObject updateMember(@RequestAttribute Member member, @RequestBody @Valid MemberUpdateDTO memberUpdateDTO){
        Long id = memberService.updateMember(member.getEmail(),memberUpdateDTO);
        Member updateMember = memberService.selectMemberById(id);

        MemberResponse memberResponse = new MemberResponse();
        modelMapper.map(updateMember,memberResponse);
        memberResponse.setToken(jwtUtil.generateToken(updateMember.getEmail()));
        return getReturnJsonObject(memberResponse);
    }

    public JSONObject getReturnJsonObject(MemberResponse memberResponse){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user",memberResponse);
        return jsonObject;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class MemberResponse {
        private String email;
        private String token;
        private String username;
        private String bio;
        private String image;
    }

}
