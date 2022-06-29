package com.kr.realworldspringboot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
public class ArticleUpdateDTO {
    @Valid
    private ArticleUpdateDTO.Article article;

    public String getTitle() { return article.getTitle(); }

    public String getDescription() { return article.getDescription(); }

    public String getBody() { return article.getBody(); }

    @Data
    @NoArgsConstructor
    class Article {
        private String title;
        private String description;
        private String body;
    }
}
