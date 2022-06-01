package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.MemberRegiDTO;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public Member registerMember(MemberRegiDTO memberRegiDTO) {
        Member member = regiDtoToEntity(memberRegiDTO);
        memberRepository.save(member);
        return member;
    }

}
