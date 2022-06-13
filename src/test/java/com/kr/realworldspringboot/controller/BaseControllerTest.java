package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.entity.Follow;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.repository.MemberRepository;
import com.kr.realworldspringboot.repository.ProfileRepository;
import com.kr.realworldspringboot.util.JWTUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
@AutoConfigureMockMvc
public class BaseControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProfileRepository profileRepository;

    protected static JWTUtil jwtUtil;
    protected static String test01token;
    protected static String test01tokenHeader;

    public static final String AUTHORIZATION = "authorization";
    public static final String TEST_01_REALWORLD_COM = "test01@realworld.com";
    public static final String TEST_02_REALWORLD_COM = "test02@realworld.com";
    public static final String TEST_01 = "test01";
    public static final String TEST = "test";

    @BeforeAll
    static void makeToken() throws Exception{
        jwtUtil = new JWTUtil();
        test01token = jwtUtil.generateToken(TEST_01_REALWORLD_COM);
        test01tokenHeader = "Bearer " + test01token;
    }

    /**
     * 기초 데이터
     * test01@realworld.com ~ test10@realworld.com 까지
     * 비밀번호 1
     *
     * 모두 test03을 구독하고있음
     *
     */
    @BeforeEach
    void defaultUserInsert(){
        //given
        for (int i = 0; i < 10; i++) {
            Member member = Member.builder()
                    .username(TEST+ "0" +i)
                    .password("$2a$10$OkMhBM2HZi0beVdSpuatRu7ACdTdQM/qIttvPcNWnTtsb9QJOXazG")
                    .email("test0"+i+"@realworld.com")
                    .build();

            memberRepository.save(member);

            Follow follow = Follow.builder()
                    .memberId(member.getId())
                    .username(FOLLOWED_USER)
                    .build();

            profileRepository.save(follow);
        }

        Member member = Member.builder()
                .username(TEST+ "11")
                .password("$2a$10$OkMhBM2HZi0beVdSpuatRu7ACdTdQM/qIttvPcNWnTtsb9QJOXazG")
                .email("test11@realworld.com")
                .build();

        memberRepository.save(member);

    }

    @AfterEach
    void deleteUser(){
        memberRepository.deleteAll();
    }


    @AfterEach
    void delete_follow(){
        profileRepository.deleteAll();
    }

    public static final String FOLLOWED_USER = "test03";
}
