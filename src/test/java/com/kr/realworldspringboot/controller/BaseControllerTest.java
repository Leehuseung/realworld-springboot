package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Comment;
import com.kr.realworldspringboot.entity.Follow;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.repository.ArticleRepository;
import com.kr.realworldspringboot.repository.CommentRepository;
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

    @Autowired
    CommentRepository commentRepository;

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
    protected void defaultInsert(){
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
        }

        for (int i = 0; i < userCnt; i++) {
            insertComment("slug"+i,"comment_body"+i,i);
        }

    }

    @AfterEach
    protected void after_delete(){
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        memberRepository.deleteAll();
        profileRepository.deleteAll();
    }

    protected void insertMember(String username){
        Member member = Member.builder()
                .username(username)
                .password("$2a$10$OkMhBM2HZi0beVdSpuatRu7ACdTdQM/qIttvPcNWnTtsb9QJOXazG")
                .email(username+"@realworld.com")
                .build();

        memberRepository.save(member);
    }

    protected void insertFollow(String username, String followUsername) {

        Member member = memberRepository.findMemberByUsername(username);
        Member followMember = memberRepository.findMemberByUsername(followUsername);
        //팔로우
        Follow follow = Follow.builder()
                .memberId(member.getId())
                .followMemberId(followMember.getId())
                .build();

        profileRepository.save(follow);
    }

    protected void insertArticle(String username, int i) {
        Member member = memberRepository.findMemberByUsername(username);

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

    protected void insertComment(String slug, String body, int i) {
        Member member = memberRepository.findMemberByUsername("test0"+i);
        Article article = articleRepository.findBySlug(slug);
        LocalDateTime ldt = LocalDateTime.now();
        Comment comment = Comment.builder()
                .id(article.getId())
                .createdAt(ldt)
                .updatedAt(ldt)
                .body(body)
                .article(article)
                .memberId(member.getId())
                .build();

        commentRepository.save(comment);
    }
}
