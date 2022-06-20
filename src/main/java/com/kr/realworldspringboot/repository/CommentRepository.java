package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.dto.CommentResponseDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Comment;
import com.kr.realworldspringboot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByArticle(Article article);

    @Query(value = "select new com.kr.realworldspringboot.dto.CommentResponseDTO(" +
            "              c.id," +
            "              c.body," +
            "              c.createdAt," +
            "              c.updatedAt, " +
            "              m" +
            ")" +
            "         from Comment c" +
            "        inner join Article a" +
            "           on c.article.id = a.id" +
            "        inner join Member m" +
            "           on m.id = c.memberId" +
            "        where a.slug = :slug")
    List<CommentResponseDTO> getCommentsBySlug(String slug);

}
