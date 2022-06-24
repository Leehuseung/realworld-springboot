package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.dto.ArticleUpdateDTO;
import com.kr.realworldspringboot.dto.MemberRegisterDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.ArticleFavorite;
import com.kr.realworldspringboot.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface ArticleService {

    Long createArticle(ArticleCreateDTO articleCreateDTO,Member member);

    Article getArticle(Long id);

    Article getArticleBySlug(String slug);

    void deleteArticle(long id);

    Long updateArticle(Long id, ArticleUpdateDTO articleUpdateDTO);

    boolean isFavorite(Article article, Member member);

    Long saveArticleFavorite(ArticleFavorite articleFavorite);

    Long countFavoriteByArticle(Article article);

    void deleteFavoriteByArticleAndMember(Article article, Member member);

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
