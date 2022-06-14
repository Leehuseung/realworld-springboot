package com.kr.realworldspringboot.entity;

import lombok.*;

import javax.persistence.*;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Date;
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

    //TODO comment, taglist


    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public void setSlugify(){
        String nowhitespace = WHITESPACE.matcher(title).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        this.slug = slug.toLowerCase(Locale.ENGLISH);
    }
}
