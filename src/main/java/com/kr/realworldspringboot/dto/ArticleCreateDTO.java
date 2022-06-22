package com.kr.realworldspringboot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ArticleCreateDTO {
    @Valid
    private Article article;

    public String getTitle() { return article.getTitle(); }

    public String getDescription() { return article.getDescription(); }

    public String getBody() { return article.getBody(); }

    public List<String> getTagList(){ return article.getTagList(); }

    @Data
    @NoArgsConstructor
    class Article {
        @NotEmpty(message = "title")
        private String title;
        @NotEmpty(message = "description")
        private String description;
        @NotEmpty(message = "body")
        private String body;
        private List<String> tagList;
    }
}




