package com.kr.realworldspringboot.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagDTO {

    private Long id;
    private String name;
    private long count;

    @QueryProjection
    public TagDTO(Long id, String name, long count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }
}
