package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.dto.ArticleUpdateDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public void deleteArticle(long id) {
        Article article = articleRepository.findById(id).get();
        articleRepository.delete(article);
    }

    @Override
    public Long updateArticle(Long id, ArticleUpdateDTO articleUpdateDTO) {
        Article article = articleRepository.findById(id).get();
        article.setTitle(articleUpdateDTO.getTitle());
        article.setSlugify();
        article.setDescription(articleUpdateDTO.getDescription());
        article.setBody(articleUpdateDTO.getBody());
        article.setUpdatedAt(LocalDateTime.now());
        articleRepository.save(article);
        return article.getId();
    }


}
