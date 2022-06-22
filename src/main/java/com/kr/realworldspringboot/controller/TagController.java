package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.entity.Tag;
import com.kr.realworldspringboot.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/api/tags")
    public JSONObject tagList() {
        JSONObject jsonObject = new JSONObject();
        List<String> arr = new ArrayList<>();
        List<Tag> list = tagService.getTagList();

        for (int i = 0; i < list.size(); i++) {
            arr.add(list.get(i).getName());
        }

        jsonObject.put("tags",arr);

        return jsonObject;
    }

}
