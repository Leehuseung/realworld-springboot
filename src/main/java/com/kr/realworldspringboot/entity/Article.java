package com.kr.realworldspringboot.entity;

import lombok.*;

import javax.persistence.*;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue
    @Column(name = "ARTICLE_ID")
    Long id;

    private String slug;
    private String title;
    private String description;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    Member member;

    @OneToMany(mappedBy = "article")
    List<ArticleTag> articleTags = new ArrayList<>();



    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public void setSlugify(){
        String noWhitespace = WHITESPACE.matcher(title).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        this.slug = slug.toLowerCase(Locale.ENGLISH);
    }

    public void addArticleTag(ArticleTag articleTag){
        articleTags.add(articleTag);
    }
}
