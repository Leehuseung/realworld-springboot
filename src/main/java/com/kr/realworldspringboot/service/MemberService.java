package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.MemberRegisterDTO;
import com.kr.realworldspringboot.dto.MemberUpdateDTO;
import com.kr.realworldspringboot.entity.Member;

public interface MemberService {

    String registerMember(MemberRegisterDTO memberRegisterDTO);

    Member selectMemberById(String id);

    String updateMember(MemberUpdateDTO memberUpdateDTO);

    Member selectMemberByUsername(String username);

    default Member regiDtoToEntity(MemberRegisterDTO memberRegisterDTO) {
        Member member = Member.builder()
                .email(memberRegisterDTO.getEmail())
                .username(memberRegisterDTO.getUsername())
                .password(memberRegisterDTO.getPassword())
                .build();

        return member;
    }
}
