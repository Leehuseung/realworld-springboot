package com.kr.realworldspringboot.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleFavorite {

    @Id
    @GeneratedValue
    @Column(name = "favorite_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    Article article;

    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;

}
