package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.MemberRegiDTO;
import com.kr.realworldspringboot.entity.Member;

public interface MemberService {

    Member registerMember(MemberRegiDTO memberRegiDTO);

    Member selectMemberById(String id);

    default Member regiDtoToEntity(MemberRegiDTO memberRegiDTO) {
        Member member = Member.builder()
                .email(memberRegiDTO.getEmail())
                .username(memberRegiDTO.getUsername())
                .password(memberRegiDTO.getPassword())
                .build();

        return member;
    }
}
