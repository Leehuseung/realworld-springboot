package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/api/tags")
    public JSONObject tagList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tags",tagService.getTagNameList());
        return jsonObject;
    }

}
