package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.dto.ArticleDTO;
import com.kr.realworldspringboot.dto.ArticleUpdateDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.repository.ArticleSearch;
import net.minidev.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface ArticleService {

    Long createArticle(ArticleCreateDTO articleCreateDTO,Long memberId);

    ArticleDTO getArticle(Long id,Long memberId);

    Article getArticleBySlug(String slug);

    void deleteArticle(Long id,Long memberId);

    Long updateArticle(Long id, Long memberId, ArticleUpdateDTO articleUpdateDTO);

    boolean isFavorite(Article article, Long memberId);

    Long saveArticleFavorite(Long ArticleId, Long memberId);

    Long countFavoriteByArticle(Article article);

    void deleteFavoriteByArticleAndMember(Article article, Member member);

    JSONObject getArticles(ArticleSearch articleSearch, Long memberId);

    JSONObject getFeeds(ArticleSearch articleSearch, Long memberId);

    default Article createDtoToEntity(ArticleCreateDTO articleCreateDTO) {
        LocalDateTime date = LocalDateTime.now();
        Article article = Article.builder()
                .title(articleCreateDTO.getTitle())
                .description(articleCreateDTO.getDescription())
                .createdAt(date)
                .updatedAt(date)
                .body(articleCreateDTO.getBody())
                .articleTags(new ArrayList<>())
                .build();

        article.setSlugify();
        return article;
    }
}
