package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Article findBySlug(String slug);
}
