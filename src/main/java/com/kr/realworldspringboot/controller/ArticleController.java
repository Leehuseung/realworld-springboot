package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.dto.ArticleUpdateDTO;
import com.kr.realworldspringboot.dto.AuthorDTO;
import com.kr.realworldspringboot.entity.*;
import com.kr.realworldspringboot.repository.ArticleSearch;
import com.kr.realworldspringboot.service.ArticleService;
import com.kr.realworldspringboot.service.MemberService;
import com.kr.realworldspringboot.service.ProfileService;
import com.kr.realworldspringboot.util.JWTUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class ArticleController {

    private final JWTUtil jwtUtil;
    private final ArticleService articleService;
    private final MemberService memberService;
    private final ProfileService profileService;

    @GetMapping("/api/articles")
    public JSONObject getArticles(@RequestHeader Map<String, Object> requestHeader, @ModelAttribute("articleSearch") ArticleSearch articleSearch) {
        String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));
        JSONObject jsonObject = new JSONObject();
        List<Article> list = articleService.getArticles(articleSearch);

        List<ArticleCreateResponse> responseList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Article article = list.get(i);

            Member member = article.getMember();

            AuthorDTO build = AuthorDTO.builder()
                    .username(member.getUsername())
                    .bio(member.getBio())
                    .image(member.getImage())
                    .following(profileService.isFollow(email, article.getMember().getUsername()))
                    .build();
            AuthorDTO author = build;

            ArticleCreateResponse articleCreateResponse = ArticleCreateResponse.builder()
                    .author(author)
                    .slug(article.getSlug())
                    .title(article.getTitle())
                    .description(article.getDescription())
                    .body(article.getBody())
                    .createdAt(article.getCreatedAt())
                    .updatedAt(article.getUpdatedAt())
                    .favorited(articleService.isFavorite(article,member))
                    .favoritesCount(articleService.countFavoriteByArticle(article))
                    .build();

            responseList.add(articleCreateResponse);
        }

        jsonObject.put("articles",responseList);

        return jsonObject;
    }

    @GetMapping("/api/articles/{slug}")
    public ResultArticle getArticle(@RequestHeader Map<String, Object> requestHeader, @PathVariable String slug) {
        String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));
        Article article = articleService.getArticleBySlug(slug);
        List<ArticleTag> tagList = article.getArticleTags();
        Member member = article.getMember();

        AuthorDTO build = AuthorDTO.builder()
                .username(member.getUsername())
                .bio(member.getBio())
                .image(member.getImage())
                .following(profileService.isFollow(email, article.getMember().getUsername()))
                .build();
        AuthorDTO author = build;

        ArticleCreateResponse articleCreateResponse = ArticleCreateResponse.builder()
                .author(author)
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .favorited(articleService.isFavorite(article,member))
                .favoritesCount(articleService.countFavoriteByArticle(article))
                .build();

        articleCreateResponse.setTagList(new ArrayList<>());

        for (int i = 0; i < tagList.size(); i++) {
            Tag tag = tagList.get(i).getTag();
            articleCreateResponse.getTagList().add(tag.getName());
        }

        return new ResultArticle(articleCreateResponse);
    }

    @PostMapping("/api/articles")
    public ResultArticle createArticle(@RequestHeader Map<String, Object> requestHeader,@RequestBody @Valid ArticleCreateDTO articleCreateDTO){
        String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));
        Member member = memberService.selectByEmail(email);

        Long id = articleService.createArticle(articleCreateDTO,member);

        Article article = articleService.getArticle(id);

        List<ArticleTag> tagList = article.getArticleTags();

        AuthorDTO build = AuthorDTO.builder()
                .username(member.getUsername())
                .bio(member.getBio())
                .image(member.getImage())
                .following(profileService.isFollow(email, member.getUsername()))
                .build();
        AuthorDTO author = build;

        ArticleCreateResponse articleCreateResponse = ArticleCreateResponse.builder()
                .author(author)
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .favorited(articleService.isFavorite(article,member))
                .favoritesCount(articleService.countFavoriteByArticle(article))
                .build();

        articleCreateResponse.setTagList(new ArrayList<>());

        for (int i = 0; i < tagList.size(); i++) {
            Tag tag = tagList.get(i).getTag();
            articleCreateResponse.getTagList().add(tag.getName());
        }

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

    @PutMapping("/api/articles/{slug}")
    public ResultArticle updateArticle(@RequestHeader Map<String, Object> requestHeader
            , @PathVariable String slug, @RequestBody @Valid ArticleUpdateDTO articleUpdateDTO) {
        String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));

        Member member = memberService.selectByEmail(email);
        Article article = articleService.getArticleBySlug(slug);

        if(member.getId().equals(article.getMember().getId())){
            Long id = articleService.updateArticle(article.getId(),articleUpdateDTO);

            Article updtaeArticle = articleService.getArticle(id);

            AuthorDTO build = AuthorDTO.builder()
                    .username(member.getUsername())
                    .bio(member.getBio())
                    .image(member.getImage())
                    .following(false)
                    .build();
            AuthorDTO author = build;

            ArticleCreateResponse articleCreateResponse = ArticleCreateResponse.builder()
                    .author(author)
                    .slug(updtaeArticle.getSlug())
                    .title(updtaeArticle.getTitle())
                    .description(updtaeArticle.getDescription())
                    .body(updtaeArticle.getBody())
                    .createdAt(updtaeArticle.getCreatedAt())
                    .updatedAt(updtaeArticle.getUpdatedAt())
                    .favorited(articleService.isFavorite(article,member))
                    .favoritesCount(articleService.countFavoriteByArticle(article))
                    .build();

            return new ResultArticle(articleCreateResponse);
        } else {
            throw new IllegalArgumentException("not authorized");
        }
    }

    @PostMapping("/api/articles/{slug}/favorite")
    public ResultArticle favoriteArticle(@RequestHeader Map<String, Object> requestHeader, @PathVariable String slug) {
        String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));

        Member member = memberService.selectByEmail(email);
        Article article = articleService.getArticleBySlug(slug);

        ArticleFavorite articleFavorite = ArticleFavorite.builder()
                .article(article)
                .member(member)
                .build();

        articleService.saveArticleFavorite(articleFavorite);

        List<ArticleTag> tagList = article.getArticleTags();

        AuthorDTO build = AuthorDTO.builder()
                .username(member.getUsername())
                .bio(member.getBio())
                .image(member.getImage())
                .following(profileService.isFollow(email, article.getMember().getUsername()))
                .build();
        AuthorDTO author = build;

        ArticleCreateResponse articleCreateResponse = ArticleCreateResponse.builder()
                .author(author)
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .favorited(articleService.isFavorite(article,member))
                .favoritesCount(articleService.countFavoriteByArticle(article))
                .build();

        articleCreateResponse.setTagList(new ArrayList<>());

        for (int i = 0; i < tagList.size(); i++) {
            Tag tag = tagList.get(i).getTag();
            articleCreateResponse.getTagList().add(tag.getName());
        }

        return new ResultArticle(articleCreateResponse);
    }

    @DeleteMapping("/api/articles/{slug}/favorite")
    public ResultArticle unfavoriteArticle(@RequestHeader Map<String, Object> requestHeader, @PathVariable String slug) {
        String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));

        Member member = memberService.selectByEmail(email);
        Article article = articleService.getArticleBySlug(slug);

        articleService.deleteFavoriteByArticleAndMember(article,member);

        List<ArticleTag> tagList = article.getArticleTags();

        AuthorDTO build = AuthorDTO.builder()
                .username(member.getUsername())
                .bio(member.getBio())
                .image(member.getImage())
                .following(profileService.isFollow(email, article.getMember().getUsername()))
                .build();
        AuthorDTO author = build;

        ArticleCreateResponse articleCreateResponse = ArticleCreateResponse.builder()
                .author(author)
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .favorited(articleService.isFavorite(article,member))
                .favoritesCount(articleService.countFavoriteByArticle(article))
                .build();

        articleCreateResponse.setTagList(new ArrayList<>());

        for (int i = 0; i < tagList.size(); i++) {
            Tag tag = tagList.get(i).getTag();
            articleCreateResponse.getTagList().add(tag.getName());
        }

        return new ResultArticle(articleCreateResponse);
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
        List<String> tagList;
        boolean favorited;
        Long favoritesCount;
        AuthorDTO author;
    }

}
