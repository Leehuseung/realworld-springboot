package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.entity.Follow;
import com.kr.realworldspringboot.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("follow 기본값 insert 확인")
    public void follow_not_null(@Autowired MockMvc mvc) throws Exception {
        Member member = memberRepository.findByEmail(TEST_01_REALWORLD_COM).get();
        //given
        Follow follow2 = profileRepository.findByMemberId(member.getId()).get();
        //when
        assertNotNull(follow2);
    }

    @Test
    @DisplayName("로그인 안하고 사용자 프로필 가져오기")
    void get_follow_false_profile(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/profiles/test06"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile.username").value("test06"))
                .andExpect(jsonPath("$.profile.bio").isEmpty())
                .andExpect(jsonPath("$.profile.image").isEmpty())
                .andExpect(jsonPath("$.profile.following").value(false));
    }

    /**
     * test01으로 test06을 조회한다. test06여부는 데이터베이스에 존재하지 않는다.
     * @param mvc
     * @throws Exception
     */
    @Test
    @DisplayName("로그인 하고 사용자 프로필 가져오기. 사용자 follow여부는 DB에 없을 때")
    public void get_follow_login_false_profile(@Autowired MockMvc mvc) throws Exception {
        String test11 = "test11";
        mvc.perform(get("/api/profiles/"+test11).header(AUTHORIZATION,"Bearer " + test01token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile.username").value(test11))
                .andExpect(jsonPath("$.profile.bio").isEmpty())
                .andExpect(jsonPath("$.profile.image").isEmpty())
                .andExpect(jsonPath("$.profile.following").value(false));
    }

    /**
     * test01으로 test03을 조회한다. test03여부는 데이터베이스에 존재한다. true 반환
     * @param mvc
     * @throws Exception
     */
    @Test
    @DisplayName("로그인 하고 사용자 프로필 가져오기. 사용자 follow여부는 DB에 있을 때")
    public void get_follow_login_true_profile(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/profiles/"+FOLLOWED_USER).header(AUTHORIZATION,"Bearer " + test01token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile.username").value(FOLLOWED_USER))
                .andExpect(jsonPath("$.profile.bio").isEmpty())
                .andExpect(jsonPath("$.profile.image").isEmpty())
                .andExpect(jsonPath("$.profile.following").value(true));
    }

    /**
     * test01으로 test02를 Follow 한다.
     * @param mvc
     * @throws Exception
     */
    @Test
    @DisplayName("사용자 follow하기")
    public void follow_user(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("/api/profiles/test02/follow").header(AUTHORIZATION,"Bearer " + test01token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile.username").value("test02"))
                .andExpect(jsonPath("$.profile.bio").isEmpty())
                .andExpect(jsonPath("$.profile.image").isEmpty())
                .andExpect(jsonPath("$.profile.following").value(true));
    }
    /**
     * test01으로 이미 follow된 03을 follow할경우 에러가 발생해야 한다.
     * @param mvc
     * @throws Exception
     */
    @Test
    @DisplayName("사용자 중복으로 follow")
    public void follow_user_duplicate(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("/api/profiles/"+FOLLOWED_USER+"/follow").header(AUTHORIZATION,"Bearer " + test01token))
                .andExpect(status().is4xxClientError());
    }

    /**
     * 기본으로 follow돼있는 03을 unfollow한다.
     * @param mvc
     * @throws Exception
     */
    @Test
    @DisplayName("사용자 follow 취소")
    public void unfollow_user(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(delete("/api/profiles/"+FOLLOWED_USER+"/follow").header(AUTHORIZATION,"Bearer " + test01token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile.username").value(FOLLOWED_USER))
                .andExpect(jsonPath("$.profile.bio").isEmpty())
                .andExpect(jsonPath("$.profile.image").isEmpty())
                .andExpect(jsonPath("$.profile.following").value(false));
    }


}