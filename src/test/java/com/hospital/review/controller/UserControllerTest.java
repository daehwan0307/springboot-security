package com.hospital.review.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.review.domain.dto.UserJoinRequest;
import com.hospital.review.domain.dto.UserLoginRequest;
import com.hospital.review.exception.ErrorCode;
import com.hospital.review.exception.HospitalReviewAppException;
import com.hospital.review.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입성공")
    @WithMockUser
    void  join() throws Exception {
        String userName = "eoghks";
        String password="1234";
        String email="eoghks@naver.com";
        mockMvc.perform(post("/api/v1/users/join")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password,email))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 성공")
    @WithAnonymousUser
    void login_success() throws Exception{
        String userName = "eoghks";
        String password="1234";
        when(userService.login(any(),any()))
                .thenReturn("token" );
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("로그인 실패 -userName 없음")
    @WithMockUser
    void login_fail1() throws Exception{
        String userName = "eoghks";
        String password="1234";
        when(userService.login(any(),any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND,""));
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("로그인 실패 -password틀림")
    @WithAnonymousUser
    void login_fail2() throws Exception{
        String userName = "eoghks";
        String password="1234";
        when(userService.login(any(),any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.INVALID_PASSWORD,""));
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}