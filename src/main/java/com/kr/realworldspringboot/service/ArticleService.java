package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.dto.MemberRegisterDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Member;

import java.time.LocalDateTime;

public interface ArticleService {

    Long createArticle(ArticleCreateDTO articleCreateDTO,Member member);

    Article getArticle(Long id);

    default Article createDtoToEntity(ArticleCreateDTO articleCreateDTO) {
        LocalDateTime date = LocalDateTime.now();
        Article article = Article.builder()
                .title(articleCreateDTO.getTitle())
                .description(articleCreateDTO.getDescription())
                .createdAt(date)
                .updatedAt(date)
                .body(articleCreateDTO.getBody())
                //TODO TAG구현
                .build();

        article.setSlugify();
        return article;
    }
}
