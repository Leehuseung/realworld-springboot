package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.dto.CommentDTO;
import com.kr.realworldspringboot.dto.CommentRegisterDTO;
import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/articles/{slug}/comments")
    public JSONObject getComment(@RequestAttribute Member member, @PathVariable String slug){
        List<CommentDTO> comments = commentService.getComments(slug,member.getId());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("comments",comments);
        return jsonObject;
    }

    @PostMapping("/api/articles/{slug}/comments")
    public JSONObject addComment(@RequestAttribute Member member
                               , @PathVariable String slug, @RequestBody @Valid CommentRegisterDTO commentRegisterDTO){
        Long id = commentService.addComment(slug,commentRegisterDTO,member.getId());
        CommentDTO commentDTO = commentService.findById(id, member.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("comment",commentDTO);
        return jsonObject;
    }


    @DeleteMapping("/api/articles/{slug}/comments/{id}")
    public void deleteComment(@RequestAttribute Member member
            , @PathVariable String slug, @PathVariable Long id){
        commentService.deleteComment(id,member.getId());
    }

}
