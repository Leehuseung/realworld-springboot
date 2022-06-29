package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.entity.Article;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kr.realworldspringboot.entity.QArticle.article;
import static com.kr.realworldspringboot.entity.QArticleTag.articleTag;
import static com.kr.realworldspringboot.entity.QTag.tag;

@AllArgsConstructor
@Repository
public class ArticleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;


    public List<Article> getArticle(ArticleSearch articleSearch){

        List<Article> articles = jpaQueryFactory
                .select(article)
                .from(article)
//                .where(tagNameEq(articleSearch.getTag()))
                .orderBy(article.createdAt.desc())
                .offset(articleSearch.getOffset())
                .limit(articleSearch.getLimit())
                .fetch();

        return articles;

    }

    private Predicate tagNameEq(String tag) {
        return tag == null ? null : articleTag.tag.name.eq(tag);
    }

}
