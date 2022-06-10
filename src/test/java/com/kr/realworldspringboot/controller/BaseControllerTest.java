package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.repository.MemberRepository;
import com.kr.realworldspringboot.util.JWTUtil;
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
        }
    }

}
