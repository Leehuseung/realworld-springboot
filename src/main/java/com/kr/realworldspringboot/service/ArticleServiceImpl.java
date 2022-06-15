package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final ArticleRepository articleRepository;

    @Override
    public Long createArticle(ArticleCreateDTO articleCreateDTO, Member member) {
        Article article = createDtoToEntity(articleCreateDTO);
        article.setMember(member);
        articleRepository.save(article);
        return article.getId();
    }

    @Override
    public Article getArticle(Long id) {
        return articleRepository.findById(id).get();
    }

    @Override
    public Article getArticleBySlug(String slug) {
        return articleRepository.findBySlug(slug);
    }


}