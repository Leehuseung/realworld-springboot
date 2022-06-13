package com.kr.realworldspringboot.security.service;


import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.repository.MemberRepository;
import com.kr.realworldspringboot.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
class AuthUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Member> result = memberRepository.findByEmail(email);

        if(result.isEmpty()) {
            throw new UsernameNotFoundException("Check Email or Social");
        }

        Member member = result.get();

        AuthMemberDTO authMember = new AuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("DEFAULT"))
        );

        authMember.setEmail(member.getEmail());
        authMember.setUsername(member.getUsername());
        authMember.setBio(member.getBio());
        authMember.setImage(member.getImage());

        return authMember;
    }
}
