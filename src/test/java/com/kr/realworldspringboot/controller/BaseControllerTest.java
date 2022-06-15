package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Follow;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.repository.ArticleRepository;
import com.kr.realworldspringboot.repository.MemberRepository;
import com.kr.realworldspringboot.repository.ProfileRepository;
import com.kr.realworldspringboot.util.JWTUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
@AutoConfigureMockMvc
public class BaseControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    ArticleRepository articleRepository;

    protected static JWTUtil jwtUtil;
    protected static String test01token;
    protected static String test01tokenHeader;

    public static final String AUTHORIZATION = "authorization";
    public static final String TEST_01_REALWORLD_COM = "test01@realworld.com";
    public static final String TEST_02_REALWORLD_COM = "test02@realworld.com";
    public static final String FOLLOWED_USER = "test03";
    public static final String TEST_01 = "test01";
    public static final String TEST = "test";

    //Article
    public static final String TEST_ARTICLE_1 = "test-article-1";

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
     * test05가 게시글을 썼음.
     *
     */
    @BeforeEach
    void defaultInsert(){
        //User
        for (int i = 0; i < 10; i++) {
            Member member = Member.builder()
                    .username(TEST+ "0" +i)
                    .password("$2a$10$OkMhBM2HZi0beVdSpuatRu7ACdTdQM/qIttvPcNWnTtsb9QJOXazG")
                    .email("test0"+i+"@realworld.com")
                    .build();

            memberRepository.save(member);


        }

        for (int i = 0; i < 10; i++) {
            Member member = memberRepository.findMemberByUsername(TEST+ "0" +i);
            Member followMember = memberRepository.findMemberByUsername(FOLLOWED_USER);
            //팔로우
            Follow follow = Follow.builder()
                    .memberId(member.getId())
                    .followMemberId(followMember.getId())
                    .build();

            profileRepository.save(follow);
        }

        Member member = Member.builder()
                .username(TEST+ "11")
                .password("$2a$10$OkMhBM2HZi0beVdSpuatRu7ACdTdQM/qIttvPcNWnTtsb9QJOXazG")
                .email("test11@realworld.com")
                .build();

        memberRepository.save(member);

        Member test05_member = memberRepository.findByEmail("test05@realworld.com").get();

        LocalDateTime ldt = LocalDateTime.now();
        Article article = Article.builder()
                .slug(TEST_ARTICLE_1)
                .title("test article 1")
                .description("this is test article description")
                .body("test article body")
                .createdAt(ldt)
                .updatedAt(ldt)
                .member(test05_member)
                .build();

        articleRepository.save(article);

    }

    @AfterEach
    void after_delete(){
        articleRepository.deleteAll();
        memberRepository.deleteAll();
        profileRepository.deleteAll();
    }
}
