package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TagControllerTest extends BaseControllerTest {

    @BeforeEach
    void insert_tags() throws Exception {
        for (int i = 1; i <= 20; i++) {
            Tag tag = Tag.builder()
                    .name("tag"+i)
                    .build();

            tagRepository.save(tag);
        }
    }

    @Test
    @DisplayName("태그 가져오기 테스트")
    void get_tags(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags[0]").value("tag20"))
                .andExpect(jsonPath("$.tags[9]").value("tag11"))
                .andExpect(jsonPath("$.tags[10]").doesNotHaveJsonPath());
    }
}