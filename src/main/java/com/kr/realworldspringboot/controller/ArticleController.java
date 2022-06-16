package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.service.ArticleService;
import com.kr.realworldspringboot.service.MemberService;
import com.kr.realworldspringboot.service.ProfileService;
import com.kr.realworldspringboot.util.JWTUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class ArticleController {

    private final JWTUtil jwtUtil;
    private final ArticleService articleService;
    private final MemberService memberService;
    private final ProfileService profileService;

    @GetMapping("/api/articles/{slug}")
    public ResultArticle getArticle(@RequestHeader Map<String, Object> requestHeader, @PathVariable String slug) {
        String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));
        Article article = articleService.getArticleBySlug(slug);
        Member member = article.getMember();

        Author build = Author.builder()
                .username(member.getUsername())
                .bio(member.getBio())
                .image(member.getImage())
                .following(profileService.isFollow(email, article.getMember().getUsername()))
                .build();
        Author author = build;

        ArticleCreateResponse articleCreateResponse = ArticleCreateResponse.builder()
                .author(author)
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();

        return new ResultArticle(articleCreateResponse);
    }

    @PostMapping("/api/articles")
    public ResultArticle createArticle(@RequestHeader Map<String, Object> requestHeader,@RequestBody @Valid ArticleCreateDTO articleCreateDTO){
        String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));
        Member member = memberService.selectByEmail(email);

        Long id = articleService.createArticle(articleCreateDTO,member);
        Article article = articleService.getArticle(id);

        Author build = Author.builder()
                .username(member.getUsername())
                .bio(member.getBio())
                .image(member.getImage())
                .following(profileService.isFollow(email, member.getUsername()))
                .build();
        Author author = build;

        ArticleCreateResponse articleCreateResponse = ArticleCreateResponse.builder()
                .author(author)
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();

        return new ResultArticle(articleCreateResponse);
    }

    @DeleteMapping("/api/articles/{slug}")
    public void deleteArticle(@RequestHeader Map<String, Object> requestHeader, @PathVariable String slug) {
        String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));

        Member member = memberService.selectByEmail(email);
        Article article = articleService.getArticleBySlug(slug);

        if(member.getId() == article.getMember().getId()){
            articleService.deleteArticle(article.getId());
        } else {
            throw new IllegalArgumentException("not authorized");
        }
    }

    @Data
    @AllArgsConstructor
    static class ResultArticle {
        ArticleController.ArticleCreateResponse article;
    }

    @Data
    @AllArgsConstructor
    @Builder
    static class ArticleCreateResponse {
        String slug;
        String title;
        String description;
        String body;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
//        List//TODO tagList 구현
//        boolean favorited; //TODO favorite 구현
//        int favoriteCount; //TODO favoritesCount 구현
        Author author;
    }

    @Data
    @AllArgsConstructor
    @Builder
    static class Author {
        String username;
        String bio;
        String image;
        boolean following;
    }
}
