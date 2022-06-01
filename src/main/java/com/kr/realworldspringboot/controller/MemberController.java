package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.dto.MemberRegiDTO;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/users")
    public Member register(@RequestBody @Valid MemberRegiDTO memberRegiDTO){
        Member member = memberService.registerMember(memberRegiDTO);
        return member;
    }
}
