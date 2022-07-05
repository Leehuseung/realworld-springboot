package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.dto.ArticleDTO;
import com.kr.realworldspringboot.dto.ArticleUpdateDTO;
import com.kr.realworldspringboot.dto.AuthorDTO;
import com.kr.realworldspringboot.entity.*;
import com.kr.realworldspringboot.repository.ArticleSearch;
import com.kr.realworldspringboot.service.ArticleService;
import com.kr.realworldspringboot.service.MemberService;
import com.kr.realworldspringboot.service.ProfileService;
import com.kr.realworldspringboot.util.JWTUtil;
import com.kr.realworldspringboot.util.LocalDateUtcParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
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
    private final LocalDateUtcParser localDateUtcParser;

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
                    .createdAt(localDateUtcParser.localDateTimeParseUTC(article.getCreatedAt()))
                    .updatedAt(localDateUtcParser.localDateTimeParseUTC(article.getUpdatedAt()))
                    .favorited(articleService.isFavorite(article,member.getId()))
                    .favoritesCount(articleService.countFavoriteByArticle(article))
                    .build();

            articleCreateResponse.setTagList(new ArrayList<>());

            responseList.add(articleCreateResponse);
        }
        jsonObject.put("articlesCount",articleService.getArticleCount(articleSearch));
        jsonObject.put("articles",responseList);

        return jsonObject;
    }

    @GetMapping("/api/articles/{slug}")
    public JSONObject getArticle(@RequestAttribute Member member, @PathVariable String slug) {
        Article article = articleService.getArticleBySlug(slug);
        ArticleDTO articleDTO = articleService.getArticle(article.getId(),member.getEmail() == null ? null : member.getId());
        return getReturnJsonObject(articleDTO);
    }

    @PostMapping("/api/articles")
    public JSONObject createArticle(@RequestAttribute Member member,@RequestBody @Valid ArticleCreateDTO articleCreateDTO){
        Long id = articleService.createArticle(articleCreateDTO,member.getId());
        ArticleDTO articleDTO = articleService.getArticle(id,member.getId());
        return getReturnJsonObject(articleDTO);
    }

    @DeleteMapping("/api/articles/{slug}")
    public void deleteArticle(@RequestAttribute Member member, @PathVariable String slug) {
        Article article = articleService.getArticleBySlug(slug);
        articleService.deleteArticle(article.getId(),member.getId());
    }

    @PutMapping("/api/articles/{slug}")
    public JSONObject updateArticle(@RequestAttribute Member member
            , @PathVariable String slug, @RequestBody @Valid ArticleUpdateDTO articleUpdateDTO) {
        Article article = articleService.getArticleBySlug(slug);
        Long id = articleService.updateArticle(article.getId(),member.getId() ,articleUpdateDTO);
        ArticleDTO articleDTO = articleService.getArticle(id,member.getId());
        return getReturnJsonObject(articleDTO);
    }

    @PostMapping("/api/articles/{slug}/favorite")
    public JSONObject favoriteArticle(@RequestAttribute Member member, @PathVariable String slug) {
        Article article = articleService.getArticleBySlug(slug);
        articleService.saveArticleFavorite(article.getId(), member.getId());
        ArticleDTO articleDTO = articleService.getArticle(article.getId(),member.getId());
        return getReturnJsonObject(articleDTO);
    }

    @DeleteMapping("/api/articles/{slug}/favorite")
    public JSONObject unFavoriteArticle(@RequestAttribute Member member, @PathVariable String slug) {
        Article article = articleService.getArticleBySlug(slug);
        articleService.deleteFavoriteByArticleAndMember(article,member);
        ArticleDTO articleDTO = articleService.getArticle(article.getId(), member.getId());
        return getReturnJsonObject(articleDTO);
    }

    public JSONObject getReturnJsonObject(ArticleDTO articleDTO){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("article",articleDTO);
        return jsonObject;
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
        ZonedDateTime createdAt;
        ZonedDateTime updatedAt;
        List<String> tagList;
        boolean favorited;
        Long favoritesCount;
        AuthorDTO author;
    }

}
