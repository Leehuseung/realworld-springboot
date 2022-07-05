package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.dto.ArticleDTO;
import com.kr.realworldspringboot.dto.ArticleUpdateDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.repository.ArticleSearch;
import com.kr.realworldspringboot.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Log4j2
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/api/articles")
    public JSONObject getArticles(@RequestAttribute Member member, @ModelAttribute("articleSearch") ArticleSearch articleSearch) {
        JSONObject jsonObject = articleService.getArticles(articleSearch,member.getId());
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

}
