package com.igar15.training_management.controller;

import com.igar15.training_management.AbstractControllerTest;
import com.igar15.training_management.UserTestData;
import com.igar15.training_management.constants.SecurityConstant;
import com.igar15.training_management.entity.User;
import com.igar15.training_management.security.UserPrincipal;
import com.igar15.training_management.service.UserService;
import com.igar15.training_management.to.UserTo;
import com.igar15.training_management.utils.JsonUtil;
import com.igar15.training_management.utils.JwtTokenProvider;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JsonUtil jsonUtil;

    private HttpHeaders jwtHeader;

    @PostConstruct
    public void createAuthorizationToken() {
        User user = userService.getUserByEmail(UserTestData.USER1.getEmail());
        UserPrincipal userPrincipal = new UserPrincipal(user);
        jwtHeader = new HttpHeaders();
        jwtHeader.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtTokenProvider.generateAuthorizationToken(userPrincipal));
    }

    @Test
    void login() {
    }

    @Test
    void getUser() throws Exception {
        perform(MockMvcRequestBuilders.get("/users/1000").headers(jwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(mvcResult -> Assertions.assertThat(jsonUtil.readValue(mvcResult.getResponse().getContentAsString(), User.class))
                        .usingRecursiveComparison().ignoringFields("registered", "emailVerificationToken", "password").isEqualTo(UserTestData.USER1));
    }

    @Test
    void getUsers() {
    }

    @Test
    void createUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void enableUser() {
    }

    @Test
    void verifyEmailToken() {
    }

    @Test
    void requestPasswordReset() {
    }

    @Test
    void resetPassword() {
    }
}