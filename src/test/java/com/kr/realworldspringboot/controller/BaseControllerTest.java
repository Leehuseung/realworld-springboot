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

    public static final String TEST = "test";
    public static final String TEST_01 = "test01";

    protected static JWTUtil jwtUtil;
    protected static String test01token;
    protected static String test05token;
    protected static String test01tokenHeader;
    protected static String test05tokenHeader;

    public static final String AUTHORIZATION = "authorization";
    public static final String TEST_01_REALWORLD_COM = "test01@realworld.com";
    public static final String TEST_02_REALWORLD_COM = "test02@realworld.com";
    public static final String TEST_05_REALWORLD_COM = "test05@realworld.com";
    public static final String FOLLOWED_USER = "test03";


    //Article
    public static final String TEST_ARTICLE_1_SLUG = "test-article-1";
    public static final String TEST_ARTICLE_1_TITLE = "test article 1";
    public static final String TEST_ARTICLE_1_DESCRIPTION = "this is test article description";
    public static final String TEST_ARTICLE_1_BODY = "test article body";

    public static final String SLUG = "slug";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String BODY = "body";



    @BeforeAll
    static void makeToken() throws Exception {
        jwtUtil = new JWTUtil();
        test01token = jwtUtil.generateToken(TEST_01_REALWORLD_COM);
        test01tokenHeader = "Bearer " + test01token;

        test05token = jwtUtil.generateToken(TEST_05_REALWORLD_COM);
        test05tokenHeader = "Bearer " + test05token;
    }

    /**
     * 기초 데이터
     * test01@realworld.com ~ test09@realworld.com 까지
     * 비밀번호 1
     *
     * 모두 test03을 구독하고있음
     *
     * 각자 글을 갖고 있음. test05 별도.
     * 
     * 
     *
     */
    @BeforeEach
    void defaultInsert(){
        int userCnt = 10;

        //Insert User
        for (int i = 0; i < userCnt; i++) {
            String username = "test0"+i;
            insertMember(username);
        }

        //Insert Follow
        for (int i = 0; i < userCnt; i++) {
            String username = "test0"+i;
            insertFollow(username,FOLLOWED_USER);
        }

        //Insert Article
        for (int i = 0; i < userCnt; i++) {
            String username = "test0"+i;
            insertArticle(username,i);
            Member member = memberRepository.findMemberByUsername(username);
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
                .slug(TEST_ARTICLE_1_SLUG)
                .title(TEST_ARTICLE_1_TITLE)
                .description(TEST_ARTICLE_1_DESCRIPTION)
                .body(TEST_ARTICLE_1_BODY)
                .createdAt(ldt)
                .updatedAt(ldt)
                .member(test05_member)
                .build();

        articleRepository.save(article);

    }

    private void insertArticle(String username, int i) {
        Member member = memberRepository.findByEmail("test05@realworld.com").get();

        LocalDateTime ldt = LocalDateTime.now();
        Article article = Article.builder()
                .slug(SLUG+i)
                .title(TITLE+i)
                .description(DESCRIPTION+i)
                .body(BODY+i)
                .createdAt(ldt)
                .updatedAt(ldt)
                .member(member)
                .build();

        articleRepository.save(article);
    }

    private void insertFollow(String username, String followUsername) {

        Member member = memberRepository.findMemberByUsername(username);
        Member followMember = memberRepository.findMemberByUsername(followUsername);
        //팔로우
        Follow follow = Follow.builder()
                .memberId(member.getId())
                .followMemberId(followMember.getId())
                .build();

        profileRepository.save(follow);
    }

    @AfterEach
    void after_delete(){
        articleRepository.deleteAll();
        memberRepository.deleteAll();
        profileRepository.deleteAll();
    }

    void insertMember(String username){
        Member member = Member.builder()
                .username(username)
                .password("$2a$10$OkMhBM2HZi0beVdSpuatRu7ACdTdQM/qIttvPcNWnTtsb9QJOXazG")
                .email(username+"@realworld.com")
                .build();

        memberRepository.save(member);
    }
}
