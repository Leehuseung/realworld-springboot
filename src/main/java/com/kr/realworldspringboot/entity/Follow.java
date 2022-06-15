package com.kr.realworldspringboot.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Follow {

    @Id
    @GeneratedValue
    @Column(name = "FOLLOW_ID")
    private Long id;
    private Long memberId;
    private Long followMemberId;


}
