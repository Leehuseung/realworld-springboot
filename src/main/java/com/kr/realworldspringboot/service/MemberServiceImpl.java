package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.MemberRegisterDTO;
import com.kr.realworldspringboot.dto.MemberUpdateDTO;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.exception.DuplicateException;
import com.kr.realworldspringboot.repository.MemberRepository;
import com.kr.realworldspringboot.repository.ProfileRepository;
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
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long registerMember(MemberRegisterDTO memberRegisterDTO) {
        Member member = regiDtoToEntity(memberRegisterDTO);

        Optional<Member> findByEmailMember = memberRepository.findByEmail(member.getEmail());
        if(!findByEmailMember.isEmpty()){
            throw new DuplicateException("email");
        }

        Member findByUsernameMember = memberRepository.findMemberByUsername(member.getUsername());
        if(findByUsernameMember != null) {
            throw new DuplicateException("username");
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));

        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public Member selectMemberById(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.get();
    }

    @Override
    public Long updateMember(String email,MemberUpdateDTO memberUpdateDTO) {

        Member member = memberRepository.findByEmail(email).get();
        member.setEmail(memberUpdateDTO.getEmail());
        member.setUsername(memberUpdateDTO.getUsername());
        member.setImage(memberUpdateDTO.getImage());
        member.setBio(memberUpdateDTO.getBio());
        memberRepository.save(member);

        return member.getId();
    }

    @Override
    public Member selectMemberByUsername(String username) {
        return memberRepository.findMemberByUsername(username);
    }

    @Override
    public Member selectByEmail(String email) {
        return memberRepository.findByEmail(email).get();
    }


}
