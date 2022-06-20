package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.CommentResponseDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Comment;
import com.kr.realworldspringboot.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Long addComment(Comment comment) {
        commentRepository.save(comment);
        return comment.getId();
    }

    @Override
    public Comment findById(Long id) {
        Comment comment = commentRepository.findById(id).get();
        return comment;
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> getComments(Article article) {
        return commentRepository.findCommentsByArticle(article);
    }

    public List<CommentResponseDTO> getCommentsDTO(String slug) {
        return commentRepository.getCommentsBySlug(slug);
    }


}
