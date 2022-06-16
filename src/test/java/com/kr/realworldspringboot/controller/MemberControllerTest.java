package com.kr.realworldspringboot.controller;

import com.kr.realworldspringboot.repository.MemberRepository;
import com.kr.realworldspringboot.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MemberControllerTest extends BaseControllerTest{

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("유저 로그인 테스트")
    public void login_user(@Autowired MockMvc mvc) throws Exception {
        //given
        String body = "{\"user\":{\"email\":\""+TEST_01_REALWORLD_COM+"\",\"password\":\"1\"}}";

        //when
        mvc.perform(post("/api/users/login").content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value(TEST_01))
                .andExpect(jsonPath("$.user.email").value(TEST_01_REALWORLD_COM))
                .andExpect(jsonPath("$.user.token").isNotEmpty());
    }

    @Test
    @DisplayName("유저 등록 테스트")
    void register_user(@Autowired MockMvc mvc) throws Exception {
        //given
        String body = "{\"user\":{\"username\":\"Jacob\",\"email\":\"jake@jake.jake\",\"password\":\"jakejake\"}}";

        //when
        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value("Jacob"))
                .andExpect(jsonPath("$.user.email").value("jake@jake.jake"))
                .andExpect(jsonPath("$.user.token").isEmpty())
                .andExpect(jsonPath("$.user.bio").isEmpty())
                .andExpect(jsonPath("$.user.image").isEmpty());
    }

    @Test
    @DisplayName("유저 이름 NULL CHECK")
    void register_username_null_validate(@Autowired MockMvc mvc) throws Exception {
        //given
        String body = "{\"user\":{\"email\":\"test02@realworld.com\",\"password\":\"1\"}}";

        //when
        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors.username[0]").value("can't be blank"));
    }

    @Test
    @DisplayName("유저 이메일 NULL CHECK")
    void register_email_null_validate(@Autowired MockMvc mvc) throws Exception {
        //given
        String body = "{\"user\":{\"username\":\"Jacob\",\"password\":\"jakejake\"}}";

        //when
        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors.email[0]").value("can't be blank"));
    }

    @Test
    @DisplayName("유저 패스워드 NULL CHECK")
    void register_password_null_validate(@Autowired MockMvc mvc) throws Exception {
        //given
        String body = "{\"user\":{\"username\":\"Jacob\",\"email\":\"jake@jake.jake\"}}";

        //when
        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors.password[0]").value("can't be blank"));
    }

    @Test
    @DisplayName("유저 이메일 중복 등록 테스트")
    void register_user_email_validate(@Autowired MockMvc mvc) throws Exception {
        //given
        String body = "{\"user\":{\"username\":\"test01\",\"email\":\"test01@realworld.com\",\"password\":\"1\"}}";

        //when
        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors.email[0]").value("has already been taken"));
    }

    //TODO 다른 사용자의 업데이트 테스트 어떻게 할지 생각해보자.
//    @Test
//    @DisplayName("다른 사용자의 업데이트 테스트")
//    public void other_user_update(@Autowired MockMvc mvc) throws Exception {
//        //given
//        String body = "{\"user\":{\"email\":\"test02@realworld.com\",\"bio\":\"I like to skateboard\",\"image\":\"https://i.stack.imgur.com/xHWG8.jpg\"}}";
//        assertThrows(Exception.class, () -> {
//            mvc.perform(put("/api/user").header(AUTHORIZATION,test01tokenHeader).contentType(MediaType.APPLICATION_JSON).content(body))
//                    .andDo(print())
//                    .andExpect(status().is5xxServerError())
//            ;
//        });
//    }

    @Test
    @DisplayName("유저 이름 중복 등록 테스트")
    void register_user_username_validate(@Autowired MockMvc mvc) throws Exception {
        //given
        String body = "{\"user\":{\"username\":\"test01\",\"email\":\"test033@realworld.com\",\"password\":\"1\"}}";

        //when
        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors.username").isNotEmpty());
    }


    @Test
    @DisplayName("유저 정보 가져오기")
    void get_current_user(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/api/user").header(AUTHORIZATION,"Bearer " + test01token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value(TEST_01))
                .andExpect(jsonPath("$.user.email").value(TEST_01_REALWORLD_COM))
                .andExpect(jsonPath("$.user.token").isNotEmpty());
    }

    @Test
    @DisplayName("유저 업데이트")
    public void update_user(@Autowired MockMvc mvc) throws Exception {
        String body = "{\"user\":{\"email\":\""+TEST_01_REALWORLD_COM+"\",\"username\":\"test01\",\"bio\":\"I like to skateboard\",\"image\":\"https://i.stack.imgur.com/xHWG8.jpg\"}}";

        //when
        mvc.perform(put("/api/user").header(AUTHORIZATION,test01tokenHeader).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value(TEST_01))
                .andExpect(jsonPath("$.user.email").value(TEST_01_REALWORLD_COM))
                .andExpect(jsonPath("$.user.token").isNotEmpty())
                .andExpect(jsonPath("$.user.bio").value("I like to skateboard"))
                .andExpect(jsonPath("$.user.image").value("https://i.stack.imgur.com/xHWG8.jpg"));
    }

    /**
     * 유저 업데이트시 Follow테이블의 Username도 업데이트돼야한다.
     * test03을 test20으로 업데이트
     * test01을 이 여전히 바뀐 test20을 구독중인지?
     * @param mvc
     * @throws Exception
     */
    @Test
    @DisplayName("유저 업데이트시 Follow도 업데이트 되는지 확인")
    public void update_user_follow(@Autowired MockMvc mvc) throws Exception {
        String body = "{\"user\":{\"email\":\"test20@realworld.com\", \"username\":\"test20\" ,\"bio\":\"I like to skateboard\",\"image\":\"https://i.stack.imgur.com/xHWG8.jpg\"}}";

        String test03tokenHeader =  "Bearer " + jwtUtil.generateToken("test03@realworld.com");

        //when
        mvc.perform(put("/api/user").header(AUTHORIZATION,test03tokenHeader).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value("test20"))
                .andExpect(jsonPath("$.user.email").value("test20@realworld.com"))
                .andExpect(jsonPath("$.user.token").isNotEmpty())
                .andExpect(jsonPath("$.user.bio").value("I like to skateboard"))
                .andExpect(jsonPath("$.user.image").value("https://i.stack.imgur.com/xHWG8.jpg"));

        mvc.perform(get("/api/profiles/test20").header(AUTHORIZATION,test01tokenHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile.username").value("test20"))
                .andExpect(jsonPath("$.profile.bio").isNotEmpty())
                .andExpect(jsonPath("$.profile.image").isNotEmpty())
                .andExpect(jsonPath("$.profile.following").value(true));
    }

}