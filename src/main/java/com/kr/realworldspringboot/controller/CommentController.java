package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.dto.ArticleUpdateDTO;
import com.kr.realworldspringboot.dto.AuthorDTO;
import com.kr.realworldspringboot.dto.CommentRegisterDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Comment;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.service.ArticleService;
import com.kr.realworldspringboot.service.CommentService;
import com.kr.realworldspringboot.service.MemberService;
import com.kr.realworldspringboot.service.ProfileService;
import com.kr.realworldspringboot.util.JWTUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.transform.Result;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ArticleService articleService;
    private final ProfileService profileService;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;


    @PostMapping("/api/articles/{slug}/comments")
    public JSONObject addComment(@RequestHeader Map<String, Object> requestHeader
            , @PathVariable String slug, @RequestBody @Valid CommentRegisterDTO commentRegisterDTO){
        String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));

        Member member = memberService.selectByEmail(email);

        Article article = articleService.getArticleBySlug(slug);

        if(member.getId() == article.getMember().getId()){

            Comment comment = commentService.createDtoToEntity(commentRegisterDTO);
            comment.setMemberId(member.getId());
            comment.setArticle(article);

            Long id = commentService.addComment(comment);

            AuthorDTO authorDTO = AuthorDTO.builder()
                    .username(member.getUsername())
                    .bio(member.getBio())
                    .image(member.getImage())
                    .following(false)
                    .build();

            CommentResponse commentResponse = CommentResponse.builder()
                    .id(id)
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .body(comment.getBody())
                    .author(authorDTO)
                    .build();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("comment",commentResponse);
            return jsonObject;
        } else {
            throw new IllegalArgumentException("not authorized");
        }
    }


    @DeleteMapping("/api/articles/{slug}/comments/{id}")
    public void deleteComment(@RequestHeader Map<String, Object> requestHeader
            , @PathVariable String slug, @PathVariable Long id){

        String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));

        Member member = memberService.selectByEmail(email);

        Comment comment = commentService.findById(id);

        if(member.getId().equals(comment.getMemberId())){
            commentService.deleteComment(id);
        } else {
            throw new IllegalArgumentException("not authorized");
        }
    }

    @Data
    @AllArgsConstructor
    @Builder
    static class CommentResponse {
        Long id;
        String body;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        AuthorDTO author;
    }

}
