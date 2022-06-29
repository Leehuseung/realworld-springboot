package com.kr.realworldspringboot.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleSearch {
    private String tag;
    private String author;
    private String favorited;
    private int limit = 20;
    private int offset = 0;
}
