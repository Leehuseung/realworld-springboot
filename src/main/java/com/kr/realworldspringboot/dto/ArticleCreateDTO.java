package com.kr.realworldspringboot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
public class ArticleCreateDTO {
    @Valid
    private Article article;

    public String getTitle() { return article.getTitle(); }

    public String getDescription() { return article.getDescription(); }

    public String getBody() { return article.getBody(); }

    @Data
    @NoArgsConstructor
    class Article {
        @NotEmpty(message = "title")
        private String title;
        @NotEmpty(message = "description")
        private String description;
        @NotEmpty(message = "body")
        private String body;
        //TODO tagList 구현 필요함..
    }
}




