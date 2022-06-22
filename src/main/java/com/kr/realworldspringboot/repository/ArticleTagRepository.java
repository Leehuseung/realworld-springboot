package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.entity.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
}
