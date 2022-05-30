package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


//    @Test
//    public void load_member() {
//        Member member = memberRepository.findById("test06@realworld.com").orElse(new Member());
//        assertEquals("test06", member.getUsername());
//    }

    @Test
    public void passwordEncode(){
//        String password = "1";

//        String password = "72ae31de-8c34-4327-80e6-607e926bf248";


//        String enPw = passwordEncoder.encode(password);
//        boolean matchResult = passwordEncoder.matches("1d",enPw);

//        System.out.println(passwordEncoder.encode("1"));

        boolean matchResult = passwordEncoder.matches("1","$2a$10$OkMhBM2HZi0beVdSpuatRu7ACdTdQM/qIttvPcNWnTtsb9QJOXazG");
        assertEquals(true,matchResult);
    }



}