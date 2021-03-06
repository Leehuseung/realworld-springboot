package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.dto.TagDTO;
import com.kr.realworldspringboot.entity.Article;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kr.realworldspringboot.entity.QArticle.article;
import static com.kr.realworldspringboot.entity.QArticleFavorite.articleFavorite;
import static com.kr.realworldspringboot.entity.QArticleTag.articleTag;
import static com.kr.realworldspringboot.entity.QFollow.follow;
import static com.kr.realworldspringboot.entity.QMember.member;
import static com.kr.realworldspringboot.entity.QTag.tag;

@AllArgsConstructor
@Repository
public class ArticleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public int getArticleCount(ArticleSearch articleSearch) {
        int cnt = jpaQueryFactory
                .select(article)
                .from(article)
                .join(article.member, member)
                .where(usernameEq(articleSearch.getAuthor()))
                .fetch().size();
        return cnt;
    }

    public List<Article> getArticle(ArticleSearch articleSearch){

        List<Article> articles = jpaQueryFactory
                .select(article)
                .from(article)
                .join(article.member, member)
                .where(tagNameEq(articleSearch.getTag()),
                        usernameEq(articleSearch.getAuthor()))
                .orderBy(article.createdAt.desc())
                .offset(articleSearch.getOffset())
                .limit(articleSearch.getLimit())
                .fetch();

        return articles;
    }
    public int getArticleByTagCount(ArticleSearch articleSearch) {
        int cnt = jpaQueryFactory
                .select(articleTag.article)
                .from(articleTag)
                .join(articleTag.tag,tag)
                .where(tagNameEq(articleSearch.getTag()))
                .fetch().size();
        return cnt;
    }

    public List<Article> getArticleByTag(ArticleSearch articleSearch){
        List<Article> articles = jpaQueryFactory
                .select(articleTag.article)
                .from(articleTag)
                .join(articleTag.tag,tag)
                .where(tagNameEq(articleSearch.getTag()))
                .orderBy(articleTag.article.createdAt.desc())
                .offset(articleSearch.getOffset())
                .limit(articleSearch.getLimit())
                .fetch();

        return articles;
    }

    private Predicate tagNameEq(String tag) {
        return tag == null ? null : articleTag.tag.name.eq(tag);
    }

    private Predicate usernameEq(String usernmae) {
        return usernmae == null ? null : article.member.username.eq(usernmae);
    }

    public List<Article> getArticleByFavorite(ArticleSearch articleSearch) {
        List<Article> articles = jpaQueryFactory
                .select(articleFavorite.article)
                .from(articleFavorite)
                .join(articleFavorite.article,article)
                .join(articleFavorite.member,member)
                .where(articleFavorite.member.username.eq(articleSearch.getFavorited()))
                .orderBy(articleFavorite.article.createdAt.desc())
                .offset(articleSearch.getOffset())
                .limit(articleSearch.getLimit())
                .fetch();

        return articles;
    }

    public int getArticleByFavoriteCount(ArticleSearch articleSearch) {
        int cnt = jpaQueryFactory
                .select(articleFavorite.article)
                .from(articleFavorite)
                .join(articleFavorite.article,article)
                .join(articleFavorite.member,member)
                .where(articleFavorite.member.username.eq(articleSearch.getFavorited()))
                .fetch().size();
        return cnt;
    }

    public int getFeedsCount(Long memberId) {
        int cnt = jpaQueryFactory
                .select(article)
                .from(article)
                .join(article.member,member)
                .where(member.id.in(
                        JPAExpressions
                                .select(follow.followMemberId)
                                .from(follow)
                                .where(follow.memberId.eq(memberId))
                ))
                .fetch().size();

        return cnt;
    }

    public List<Article> getFeeds(ArticleSearch articleSearch,Long memberId) {
        List<Article> articles = jpaQueryFactory
                .select(article)
                .from(article)
                .join(article.member,member)
                .where(member.id.in(
                        JPAExpressions
                                .select(follow.followMemberId)
                                .from(follow)
                                .where(follow.memberId.eq(memberId))
                ))
                .orderBy(article.createdAt.desc())
                .offset(articleSearch.getOffset())
                .limit(articleSearch.getLimit())
                .fetch();

        return articles;
    }

    public List<TagDTO> getTags(){

        List<TagDTO> tags = jpaQueryFactory
            .select(Projections.constructor(TagDTO.class,tag.id,tag.name,tag.count()))
            .from(tag,articleTag)
            .where(tag.id.eq(articleTag.tag.id))
            .groupBy(tag.name)
            .orderBy(tag.count().desc())
            .offset(0)
            .limit(10)
            .fetch();

        return tags;
    }


}
