package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.dto.MemberRegiDTO;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.service.MemberService;
import com.kr.realworldspringboot.util.JWTUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @PostMapping("/api/users")
    public ResultMember registerMember(@RequestBody @Valid MemberRegiDTO memberRegiDTO){
        Member member = memberService.registerMember(memberRegiDTO);

        MemberRegisterResponse memberRegisterResponse = MemberRegisterResponse.builder()
                .email(member.getEmail())
                .token(null)
                .username(member.getUsername())
                .build();

        return new ResultMember(memberRegisterResponse);
    }

    @GetMapping("/api/user")
    public ResultMember getMember(@RequestHeader Map<String, Object> requestHeader) {

        String token = jwtUtil.getToken((String)requestHeader.get("authorization"));
        String email = jwtUtil.validateAndExtract(token);

        Member member = memberService.selectMemberById(email);

        MemberRegisterResponse memberRegisterResponse = MemberRegisterResponse.builder()
                .email(member.getEmail())
                .token(token)
                .username(member.getUsername())
                .bio(member.getBio())
                .image(member.getImage())
                .build();

        return new ResultMember(memberRegisterResponse);
    }

    @Data
    @AllArgsConstructor
    static class ResultMember {
        MemberRegisterResponse user;
    }

    @Data
    @AllArgsConstructor
    @Builder
    static class MemberRegisterResponse {
        private String email;
        private String token;
        private String username;
        private String bio;
        private String image;
    }

}
