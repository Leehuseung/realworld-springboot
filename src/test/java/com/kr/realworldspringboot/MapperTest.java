package com.kr.realworldspringboot;

import com.kr.realworldspringboot.entity.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class MapperTest {

    ModelMapper modelMapper = new ModelMapper();

    @Test
    @DisplayName("modelMapper_test")
    public void modelMapper_test() throws Exception {
        //given
        Member member1 = Member.builder()
                .id(1l)
                .email("email")
                .username("username")
                .build();

        Member member2 = Member.builder()
                .id(1l)
                .bio("bio")
                .image("image")
                .build();

        //when
        modelMapper.map(member1,member2);

        //then
        Assertions.assertEquals(null,member2.getBio());

    }
}
