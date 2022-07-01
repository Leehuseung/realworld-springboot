package com.kr.realworldspringboot.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "COMMENT_ID")
    Long id;

    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    Article article;
}
