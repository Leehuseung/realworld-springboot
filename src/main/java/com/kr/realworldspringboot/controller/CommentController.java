package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.dto.ArticleUpdateDTO;
import com.kr.realworldspringboot.dto.AuthorDTO;
import com.kr.realworldspringboot.dto.CommentRegisterDTO;
import com.kr.realworldspringboot.dto.CommentResponseDTO;
import com.kr.realworldspringboot.entity.Article;
import com.kr.realworldspringboot.entity.Comment;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.service.ArticleService;
import com.kr.realworldspringboot.service.CommentService;
import com.kr.realworldspringboot.service.MemberService;
import com.kr.realworldspringboot.service.ProfileService;
import com.kr.realworldspringboot.util.JWTUtil;
import com.kr.realworldspringboot.util.LocalDateUtcParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.transform.Result;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final LocalDateUtcParser localDateUtcParser;

    @GetMapping("/api/articles/{slug}/comments")
    public JSONObject getComment(@RequestHeader Map<String, Object> requestHeader, @PathVariable String slug){

        Article article = articleService.getArticleBySlug(slug);
        List<Comment> comments = commentService.getComments(article);

        List<CommentResponse> list = new ArrayList<>();

        for (int i = 0; i < comments.size(); i++) {
            //TODO 일일이 member를 select하지 않도록 개선 필요함.
            Comment comment = comments.get(i);
            memberService.selectMemberById(comment.getMemberId());
            Member writer = memberService.selectMemberById(comment.getMemberId());

            boolean isFollow = false;
            if(requestHeader.get("authorization") != null){
                String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));
                Member member = memberService.selectByEmail(email);
                isFollow = profileService.isFollow(email, writer.getUsername());
            }

            AuthorDTO authorDTO = AuthorDTO.builder()
                    .username(writer.getUsername())
                    .bio(writer.getBio())
                    .image(writer.getImage())
                    .following(isFollow)
                    .build();

            CommentResponse commentResponse = CommentResponse.builder()
                    .id(comment.getId())
                    .createdAt(localDateUtcParser.localDateTimeParseUTC(article.getCreatedAt()))
                    .updatedAt(localDateUtcParser.localDateTimeParseUTC(article.getUpdatedAt()))
                    .body(comment.getBody())
                    .author(authorDTO)
                    .build();

            list.add(commentResponse);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("comments",list);
        return jsonObject;
    }

    @PostMapping("/api/articles/{slug}/comments")
    public JSONObject addComment(@RequestHeader Map<String, Object> requestHeader
            , @PathVariable String slug, @RequestBody @Valid CommentRegisterDTO commentRegisterDTO){
        String email = jwtUtil.getEmailbyHeader((String)requestHeader.get("authorization"));

        Member member = memberService.selectByEmail(email);

        Article article = articleService.getArticleBySlug(slug);

        if(member.getId().equals(article.getMember().getId())){

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
                    .createdAt(localDateUtcParser.localDateTimeParseUTC(article.getCreatedAt()))
                    .updatedAt(localDateUtcParser.localDateTimeParseUTC(article.getUpdatedAt()))
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
        ZonedDateTime createdAt;
        ZonedDateTime updatedAt;
        AuthorDTO author;
    }

}
