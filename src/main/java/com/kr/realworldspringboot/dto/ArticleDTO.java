package com.kr.realworldspringboot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ArticleDTO {
    String slug;
    String title;
    String description;
    String body;
    ZonedDateTime createdAt;
    ZonedDateTime updatedAt;
    List<String> tagList;
    boolean favorited;
    Long favoritesCount;
    ProfileDTO author;

}
