package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.CommentDTO;
import com.kr.realworldspringboot.dto.CommentRegisterDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Comment;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.repository.ArticleRepository;
import com.kr.realworldspringboot.repository.CommentRepository;
import com.kr.realworldspringboot.repository.MemberRepository;
import com.kr.realworldspringboot.util.LocalDateUtcParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ProfileService profileService;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final LocalDateUtcParser localDateUtcParser;

    @Override
    public Long addComment(String slug, CommentRegisterDTO commentRegisterDTO, Long memberId) {
        Article article = articleRepository.findBySlug(slug);
        Member member = memberRepository.findById(memberId).get();

        Comment comment = createDtoToEntity(commentRegisterDTO);
        comment.setArticle(article);
        comment.setMember(member);
        commentRepository.save(comment);

        return comment.getId();
    }

    @Override
    public CommentDTO findById(Long id,Long memberId) {
        Comment comment = commentRepository.findById(id).get();
        Member member = comment.getMember();
        CommentDTO commentDTO = CommentDTO.builder()
                .id(comment.getId())
                .createdAt(localDateUtcParser.localDateTimeParseUTC(comment.getCreatedAt()))
                .updatedAt(localDateUtcParser.localDateTimeParseUTC(comment.getUpdatedAt()))
                .body(comment.getBody())
                .author(profileService.findProfile(member.getId(),memberId))
                .build();

        return commentDTO;
    }

    @Override
    public void deleteComment(Long id,Long memberId) {
        commentRepository.deleteByIdAndMemberEquals(id,memberId);
    }

    @Override
    public List<CommentDTO> getComments(String slug, Long memberId) {
        Article article = articleRepository.findBySlug(slug);
        List<Comment> comments = commentRepository.findCommentsByArticleOrderByCreatedAtDesc(article);
        List<CommentDTO> dtoList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = CommentDTO.builder()
                    .id(comment.getId())
                    .createdAt(localDateUtcParser.localDateTimeParseUTC(comment.getCreatedAt()))
                    .updatedAt(localDateUtcParser.localDateTimeParseUTC(comment.getUpdatedAt()))
                    .body(comment.getBody())
                    .author(profileService.findProfile(comment.getMember().getId(),memberId))
                    .build();
            dtoList.add(commentDTO);
        }

        return dtoList;
    }



}
