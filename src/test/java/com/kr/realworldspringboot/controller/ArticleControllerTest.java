package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.entity.*;
import com.kr.realworldspringboot.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ArticleControllerTest extends BaseControllerTest {

    @BeforeEach
    void insertTestArticle() throws Exception{
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

        Tag tagA = Tag.builder()
                .name("tagA")
                .build();

        tagRepository.save(tagA);

        Tag tagB = Tag.builder()
                .name("tagB")
                .build();

        tagRepository.save(tagB);

        ArticleTag articleTagA = ArticleTag.builder()
                .tag(tagA)
                .article(article)
                .build();

        articleTagRepository.save(articleTagA);

        ArticleTag articleTagB = ArticleTag.builder()
                .tag(tagB)
                .article(article)
                .build();

        articleTagRepository.save(articleTagB);


        Member test02 = memberRepository.findByEmail("test02@realworld.com").get();
        Member test03 = memberRepository.findByEmail("test03@realworld.com").get();
        Article article1 = articleRepository.findBySlug(TEST_ARTICLE_1_SLUG);
        insertArticleFavorite(article1,test02);
        insertArticleFavorite(article1,test03);


        //????????? List Test
        for (int i = 11; i <= 40; i++) {

            Member member2 = memberRepository.findMemberByUsername("test02");
            ldt = LocalDateTime.now();
            Article article2 = Article.builder()
                    .slug(SLUG+i)
                    .title(TITLE+i)
                    .description(DESCRIPTION+i)
                    .body(BODY+i)
                    .createdAt(ldt)
                    .updatedAt(ldt)
                    .member(member2)
                    .build();

            articleRepository.save(article2);

        }

        insertFollow("test02","test05");

    }

    void insertArticleFavorite(Article article, Member member){

        ArticleFavorite af = ArticleFavorite.builder()  
                .article(article)
                .member(member)
                .build();

        articleFavoriteRepository.save(af);
    }

    @Test
    @DisplayName("??? ????????? ?????????(??????????????????)")
    public void list_articles_test(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/articles").header(AUTHORIZATION,test01tokenHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles[0].slug").value("slug40"))
                .andExpect(jsonPath("$.articles[0].author.image").hasJsonPath())
                .andExpect(jsonPath("$.articles[19].slug").value("slug21"))
                .andExpect(jsonPath("$.articles[19].author.image").hasJsonPath())
                .andExpect(jsonPath("$.articlesCount").hasJsonPath());
    }

    /**
     * tagA??? ???????????? ?????? TEST_ARTICLE_1_SLUG
     * @param mvc
     * @throws Exception
     */
    @Test
    @DisplayName("??? ????????? ?????????(tag ???????????? ??????)")
    public void list_articles_test_tag(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/articles?tag=tagA").header(AUTHORIZATION,test01tokenHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles[0].slug").value(TEST_ARTICLE_1_SLUG))
                .andExpect(jsonPath("$.articles[0].title").value(TEST_ARTICLE_1_TITLE))
                .andExpect(jsonPath("$.articles[0].description").value(TEST_ARTICLE_1_DESCRIPTION))
                .andExpect(jsonPath("$.articles[0].body").value("test article body"))
                .andExpect(jsonPath("$.articles[0].tagList[0]").value("tagA"))
                .andExpect(jsonPath("$.articles[0].tagList[1]").value("tagB"))
                .andExpect(jsonPath("$.articles[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.articles[0].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.articles[0].author.username").value("test05"))
                .andExpect(jsonPath("$.articles[0].author.following").value(false))
                .andExpect(jsonPath("$.articles[0].author.bio").hasJsonPath())
                .andExpect(jsonPath("$.articles[0].author.image").hasJsonPath());
    }

    /**
     * test05??? ????????? ?????? ??????
     * @param mvc
     * @throws Exception
     */
    @Test
    @DisplayName("??? ????????? ?????????(author ???????????? ??????)")
    public void list_articles_test_author(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/articles?author=test05").header(AUTHORIZATION,test01tokenHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles[0].slug").value(TEST_ARTICLE_1_SLUG))
                .andExpect(jsonPath("$.articles[0].title").value(TEST_ARTICLE_1_TITLE))
                .andExpect(jsonPath("$.articles[0].description").value(TEST_ARTICLE_1_DESCRIPTION))
                .andExpect(jsonPath("$.articles[0].body").value("test article body"))
                .andExpect(jsonPath("$.articles[0].tagList[0]").value("tagA"))
                .andExpect(jsonPath("$.articles[0].tagList[1]").value("tagB"))
                .andExpect(jsonPath("$.articles[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.articles[0].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.articles[0].author.username").value("test05"))
                .andExpect(jsonPath("$.articles[0].author.following").value(false))
                .andExpect(jsonPath("$.articles[0].author.bio").hasJsonPath())
                .andExpect(jsonPath("$.articles[0].author.image").hasJsonPath())
                .andExpect(jsonPath("$.articles[1].slug").value("slug5"))
                .andExpect(jsonPath("$.articles[1].title").value("title5"))
                .andExpect(jsonPath("$.articles[1].description").value("description5"))
                .andExpect(jsonPath("$.articles[1].body").value("body5"))
                .andExpect(jsonPath("$.articles[1].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.articles[1].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.articles[1].author.username").value("test05"))
                .andExpect(jsonPath("$.articles[1].author.following").value(false))
                .andExpect(jsonPath("$.articles[1].author.bio").hasJsonPath())
                .andExpect(jsonPath("$.articles[1].author.image").hasJsonPath());
    }

    /**
     * test02??? test05??? TEST_ARTICLE_1_SLUG??? ????????? ??? ?????????.
     * @param mvc
     * @throws Exception
     */
    @Test
    @DisplayName("??? ????????? ?????????(favorite ???????????? ??????)")
    public void list_articles_test_favorite(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/articles?favorited=test02").header(AUTHORIZATION,test01tokenHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles[0].slug").value(TEST_ARTICLE_1_SLUG))
                .andExpect(jsonPath("$.articles[0].title").value(TEST_ARTICLE_1_TITLE))
                .andExpect(jsonPath("$.articles[0].description").value(TEST_ARTICLE_1_DESCRIPTION))
                .andExpect(jsonPath("$.articles[0].body").value("test article body"))
                .andExpect(jsonPath("$.articles[0].tagList[0]").value("tagA"))
                .andExpect(jsonPath("$.articles[0].tagList[1]").value("tagB"))
                .andExpect(jsonPath("$.articles[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.articles[0].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.articles[0].author.username").value("test05"))
                .andExpect(jsonPath("$.articles[0].author.following").value(false))
                .andExpect(jsonPath("$.articles[0].author.bio").hasJsonPath())
                .andExpect(jsonPath("$.articles[0].author.image").hasJsonPath());
    }

    @Test
    @DisplayName("??? ?????? ?????????")
    public void create_article(@Autowired MockMvc mvc) throws Exception {
        //given
        String body = "{\"article\":{\"title\":\"How to train your dragon\",\"description\":\"Ever wonder how?\",\"body\":\"You have to believe\",\"tagList\":[\"tagA\",\"tagB\"]}}";
        
        //then
        mvc.perform(post("/api/articles").header(AUTHORIZATION,test01tokenHeader).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value("how-to-train-your-dragon"))
                .andExpect(jsonPath("$.article.title").value("How to train your dragon"))
                .andExpect(jsonPath("$.article.description").value("Ever wonder how?"))
                .andExpect(jsonPath("$.article.body").value("You have to believe"))
                .andExpect(jsonPath("$.article.tagList[0]").value("tagA"))
                .andExpect(jsonPath("$.article.tagList[1]").value("tagB"))
                .andExpect(jsonPath("$.article.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.article.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.article.favorited").value(false))
                .andExpect(jsonPath("$.article.favoritesCount").value(0))
                .andExpect(jsonPath("$.article.author.username").value("test01"))
                .andExpect(jsonPath("$.article.author.following").value(false))
                .andExpect(jsonPath("$.article.author.bio").hasJsonPath())
                .andExpect(jsonPath("$.article.author.image").hasJsonPath());
    }

    @Test
    @DisplayName("?????? ?????? ?????????. ????????? ?????? ??????.")
    public void get_article_login(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/articles/"+TEST_ARTICLE_1_SLUG))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value(TEST_ARTICLE_1_SLUG))
                .andExpect(jsonPath("$.article.title").value(TEST_ARTICLE_1_TITLE))
                .andExpect(jsonPath("$.article.description").value(TEST_ARTICLE_1_DESCRIPTION))
                .andExpect(jsonPath("$.article.body").value("test article body"))
                .andExpect(jsonPath("$.article.tagList[0]").value("tagA"))
                .andExpect(jsonPath("$.article.tagList[1]").value("tagB"))
                .andExpect(jsonPath("$.article.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.article.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.article.author.username").value("test05"))
                .andExpect(jsonPath("$.article.author.following").value(false))
                .andExpect(jsonPath("$.article.author.bio").hasJsonPath())
                .andExpect(jsonPath("$.article.author.image").hasJsonPath());
    }

    @Test
    @DisplayName("??? ?????? ?????????. ????????? ?????? ??????")
    public void get_article_login_fallow_false(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/articles/"+TEST_ARTICLE_1_SLUG).header(AUTHORIZATION,test01tokenHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value(TEST_ARTICLE_1_SLUG))
                .andExpect(jsonPath("$.article.title").value(TEST_ARTICLE_1_TITLE))
                .andExpect(jsonPath("$.article.description").value(TEST_ARTICLE_1_DESCRIPTION))
                .andExpect(jsonPath("$.article.body").value(TEST_ARTICLE_1_BODY))
                .andExpect(jsonPath("$.article.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.article.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.article.author.username").value("test05"))
                .andExpect(jsonPath("$.article.author.following").value(false))
                .andExpect(jsonPath("$.article.author.bio").hasJsonPath())
                .andExpect(jsonPath("$.article.author.image").hasJsonPath());
    }

    @Test
    @DisplayName("??? ?????? ?????????. ?????????.")
    public void get_article_login_fallow_true(@Autowired MockMvc mvc) throws Exception {
        Member member = memberRepository.findByEmail("test01@realworld.com").get();

        Member followMember = memberRepository.findMemberByUsername("test05");

        //?????????
        Follow follow = Follow.builder()
                .memberId(member.getId())
                .followMemberId(followMember.getId())
                .build();

        profileRepository.save(follow);

        mvc.perform(get("/api/articles/"+TEST_ARTICLE_1_SLUG).header(AUTHORIZATION,test01tokenHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value(TEST_ARTICLE_1_SLUG))
                .andExpect(jsonPath("$.article.title").value(TEST_ARTICLE_1_TITLE))
                .andExpect(jsonPath("$.article.description").value(TEST_ARTICLE_1_DESCRIPTION))
                .andExpect(jsonPath("$.article.body").value(TEST_ARTICLE_1_BODY))
                .andExpect(jsonPath("$.article.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.article.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.article.author.username").value("test05"))
                .andExpect(jsonPath("$.article.author.following").value(true))
                .andExpect(jsonPath("$.article.author.bio").hasJsonPath())
                .andExpect(jsonPath("$.article.author.image").hasJsonPath());
    }
    
    @Test
    @DisplayName("??? ?????? ?????????")
    public void delete_article(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(delete("/api/articles/"+TEST_ARTICLE_1_SLUG).header(AUTHORIZATION,test05tokenHeader))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("??? ?????? ?????????")
    public void update_article(@Autowired MockMvc mvc) throws Exception {
        //given
        String body = "{\"article\":{\"title\":\"update title\",\"description\":\"update description\",\"body\":\"update body\"}}";

        mvc.perform(put("/api/articles/"+TEST_ARTICLE_1_SLUG).header(AUTHORIZATION,test05tokenHeader).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value("update-title"))
                .andExpect(jsonPath("$.article.title").value("update title"))
                .andExpect(jsonPath("$.article.description").value("update description"))
                .andExpect(jsonPath("$.article.body").value("update body"))
                .andExpect(jsonPath("$.article.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.article.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.article.author.username").value("test05"))
                .andExpect(jsonPath("$.article.author.following").value(false))
                .andExpect(jsonPath("$.article.author.bio").hasJsonPath())
                .andExpect(jsonPath("$.article.author.image").hasJsonPath())
                .andDo(print());
    }

    @Test
    @DisplayName("??? ????????? ?????????.")
    public void favorite_article(@Autowired MockMvc mvc) throws Exception {

        mvc.perform(post("/api/articles/"+TEST_ARTICLE_1_SLUG+"/favorite").header(AUTHORIZATION,test05tokenHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value(TEST_ARTICLE_1_SLUG))
                .andExpect(jsonPath("$.article.title").value(TEST_ARTICLE_1_TITLE))
                .andExpect(jsonPath("$.article.description").value(TEST_ARTICLE_1_DESCRIPTION))
                .andExpect(jsonPath("$.article.body").value(TEST_ARTICLE_1_BODY))
                .andExpect(jsonPath("$.article.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.article.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.article.favorited").value(true))
                .andExpect(jsonPath("$.article.favoritesCount").value(3))
                .andExpect(jsonPath("$.article.author.username").value("test05"))
                .andExpect(jsonPath("$.article.author.following").value(false))
                .andExpect(jsonPath("$.article.author.bio").hasJsonPath())
                .andExpect(jsonPath("$.article.author.image").hasJsonPath());
    }

    @Test
    @DisplayName("??? ????????? ?????? ?????????.")
    public void unfavorite_article(@Autowired MockMvc mvc) throws Exception {

        mvc.perform(delete("/api/articles/"+TEST_ARTICLE_1_SLUG+"/favorite").header(AUTHORIZATION,test02tokenHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value(TEST_ARTICLE_1_SLUG))
                .andExpect(jsonPath("$.article.title").value(TEST_ARTICLE_1_TITLE))
                .andExpect(jsonPath("$.article.description").value(TEST_ARTICLE_1_DESCRIPTION))
                .andExpect(jsonPath("$.article.body").value(TEST_ARTICLE_1_BODY))
                .andExpect(jsonPath("$.article.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.article.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.article.favorited").value(false))
                .andExpect(jsonPath("$.article.favoritesCount").value(1))
                .andExpect(jsonPath("$.article.author.username").value("test05"))
                .andExpect(jsonPath("$.article.author.following").value(true))
                .andExpect(jsonPath("$.article.author.bio").hasJsonPath())
                .andExpect(jsonPath("$.article.author.image").hasJsonPath());
    }

    /**
     * test02??? test05??? ????????????. test05??? ?????? ????????????.
     * @param mvc
     * @throws Exception
     */
    @Test
    @DisplayName("?????? ?????????")
    public void feed_test(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/articles/feed").header(AUTHORIZATION,test02tokenHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles[0].slug").value(TEST_ARTICLE_1_SLUG))
                .andExpect(jsonPath("$.articles[0].title").value(TEST_ARTICLE_1_TITLE))
                .andExpect(jsonPath("$.articles[0].description").value(TEST_ARTICLE_1_DESCRIPTION))
                .andExpect(jsonPath("$.articles[0].body").value("test article body"))
                .andExpect(jsonPath("$.articles[0].tagList[0]").value("tagA"))
                .andExpect(jsonPath("$.articles[0].tagList[1]").value("tagB"))
                .andExpect(jsonPath("$.articles[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.articles[0].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.articles[0].author.username").value("test05"))
                .andExpect(jsonPath("$.articles[0].author.following").value(true))
                .andExpect(jsonPath("$.articles[0].author.bio").hasJsonPath())
                .andExpect(jsonPath("$.articles[0].author.image").hasJsonPath())
                .andExpect(jsonPath("$.articles[1].slug").value("slug5"))
                .andExpect(jsonPath("$.articles[1].title").value("title5"))
                .andExpect(jsonPath("$.articles[1].description").value("description5"))
                .andExpect(jsonPath("$.articles[1].body").value("body5"))
                .andExpect(jsonPath("$.articles[1].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.articles[1].updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.articles[1].author.username").value("test05"))
                .andExpect(jsonPath("$.articles[1].author.following").value(true))
                .andExpect(jsonPath("$.articles[1].author.bio").hasJsonPath())
                .andExpect(jsonPath("$.articles[1].author.image").hasJsonPath());
    }


}