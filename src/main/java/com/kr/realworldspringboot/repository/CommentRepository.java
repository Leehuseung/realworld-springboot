package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.dto.CommentResponseDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Comment;
import com.kr.realworldspringboot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByArticleOrderByCreatedAtDesc(Article article);

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
            "           on m.id = c.member.id" +
            "        where a.slug = :slug")
    List<CommentResponseDTO> getCommentsBySlug(String slug);

    @Modifying
    @Transactional
    @Query(value = "delete from Comment c where c.id = :id and c.member.id = :memberId")
    void deleteByIdAndMemberEquals(Long id, Long memberId);

}
