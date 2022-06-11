package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.MemberRegisterDTO;
import com.kr.realworldspringboot.dto.MemberUpdateDTO;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.exception.DuplicateException;
import com.kr.realworldspringboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String registerMember(MemberRegisterDTO memberRegisterDTO) {
        Member member = regiDtoToEntity(memberRegisterDTO);

        Optional<Member> findByIdMember = memberRepository.findById(member.getEmail());
        if(!findByIdMember.isEmpty()){
            throw new DuplicateException("email");
        }

        Member findByUsernameMember = memberRepository.findMemberByUsername(member.getUsername());
        if(findByUsernameMember != null) {
            throw new DuplicateException("username");
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));

        memberRepository.save(member);
        return member.getEmail();
    }

    @Override
    public Member selectMemberById(String id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.get();
    }

    @Override
    public String updateMember(MemberUpdateDTO memberUpdateDTO) {

        Member member = memberRepository.findById(memberUpdateDTO.getEmail()).get();
        member.setImage(memberUpdateDTO.getImage());
        member.setBio(memberUpdateDTO.getBio());
        memberRepository.save(member);

        return member.getEmail();
    }

    @Override
    public Member selectMemberByUsername(String username) {
        return memberRepository.findMemberByUsername(username);
    }


}
