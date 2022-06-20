package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.MemberRegisterDTO;
import com.kr.realworldspringboot.dto.MemberUpdateDTO;
import com.kr.realworldspringboot.entity.Member;

public interface MemberService {

    Long registerMember(MemberRegisterDTO memberRegisterDTO);

    Member selectMemberById(Long id);

    Long updateMember(String email,MemberUpdateDTO memberUpdateDTO);

    Member selectMemberByUsername(String username);

    Member selectByEmail(String email);

    default Member regiDtoToEntity(MemberRegisterDTO memberRegisterDTO) {
        Member member = Member.builder()
                .email(memberRegisterDTO.getEmail())
                .username(memberRegisterDTO.getUsername())
                .password(memberRegisterDTO.getPassword())
                .build();

        return member;
    }
}
