package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.ArticleFavorite;
import com.kr.realworldspringboot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavorite, Long> {

    Long countByArticleAndMember(Article article, Member member);

    ArticleFavorite findByArticleAndMember(Article article, Member member);

    Long countByArticle(Article article);

    void deleteArticleFavoriteByArticleAndMember(Article article, Member member);

    void deleteArticleFavoritesByArticle(Article article);


}
