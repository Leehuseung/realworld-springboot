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
    private Long id;
    private Long memberId;
    private String username;


}
