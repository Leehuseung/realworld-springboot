package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.MemberRegisterDTO;
import com.kr.realworldspringboot.dto.MemberUpdateDTO;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.exception.DuplicateException;
import com.kr.realworldspringboot.repository.MemberRepository;
import com.kr.realworldspringboot.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.Condition;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

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

        //validation check
        if(!member.getEmail().equals(memberUpdateDTO.getEmail())){
            if(memberRepository.countMemberByEmail(memberUpdateDTO.getEmail()) > 0){
                throw new DuplicateException("email");
            }
        }
        if(!member.getUsername().equals(memberUpdateDTO.getUsername())){
            if(memberRepository.countMemberByUsername(memberUpdateDTO.getUsername()) > 0){
                throw new DuplicateException("username");
            }
        }

        if(memberUpdateDTO.getPassword() != null){
            memberUpdateDTO.setPassword(passwordEncoder.encode(memberUpdateDTO.getPassword()));
        }
        if(memberUpdateDTO.getPassword() != null && memberUpdateDTO.getPassword().equals("")){
            memberUpdateDTO.setPassword(member.getPassword());
        }
        if(memberUpdateDTO.getEmail().equals("")){
            memberUpdateDTO.setEmail(member.getEmail());
        }

        modelMapper.map(memberUpdateDTO,member);

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
