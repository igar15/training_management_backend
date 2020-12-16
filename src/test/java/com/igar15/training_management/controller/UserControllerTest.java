package com.igar15.training_management.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.igar15.training_management.AbstractControllerTest;
import com.igar15.training_management.constants.SecurityConstant;
import com.igar15.training_management.entity.PasswordResetToken;
import com.igar15.training_management.entity.User;
import com.igar15.training_management.exceptions.MyEntityNotFoundException;
import com.igar15.training_management.repository.PasswordResetTokenRepository;
import com.igar15.training_management.security.UserPrincipal;
import com.igar15.training_management.service.LoginAttemptService;
import com.igar15.training_management.service.UserService;
import com.igar15.training_management.to.MyHttpResponse;
import com.igar15.training_management.to.PasswordResetModel;
import com.igar15.training_management.to.UserTo;
import com.igar15.training_management.utils.JsonUtil;
import com.igar15.training_management.utils.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

import static com.igar15.training_management.ControllerTestData.*;
import static com.igar15.training_management.UserTestData.*;
import static org.assertj.core.api.Assertions.*;

class UserControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private LoginAttemptService loginAttemptService;

    private HttpHeaders userJwtHeader;

    private HttpHeaders adminJwtHeader;

    @PostConstruct
    public void createAuthorizationToken() {
        User user = userService.getUserByEmail(USER1.getEmail());
        UserPrincipal userPrincipal = new UserPrincipal(user);
        userJwtHeader = new HttpHeaders();
        userJwtHeader.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtTokenProvider.generateAuthorizationToken(userPrincipal));
        User admin = userService.getUserByEmail(ADMIN.getEmail());
        UserPrincipal adminUserPrincipal = new UserPrincipal(admin);
        adminJwtHeader = new HttpHeaders();
        adminJwtHeader.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtTokenProvider.generateAuthorizationToken(adminUserPrincipal));
    }

    @Test
    void login() throws Exception {
        UserTo userTo = getUserToForLogin();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(userTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Assertions.assertNotNull(resultActions.andReturn().getResponse().getHeader(SecurityConstant.JWT_AUTHORIZATION_TOKEN_HEADER));
        User user = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), User.class);
        assertThat(user).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "password").isEqualTo(USER1);
    }

    @Test
    void loginWhenBadCredentialsPassed() throws Exception {
        UserTo userTo = getUserToForLogin();
        userTo.setPassword("12345678");
        ResultActions resultActions = perform(MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(userTo)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Assertions.assertNull(resultActions.andReturn().getResponse().getHeader(SecurityConstant.JWT_AUTHORIZATION_TOKEN_HEADER));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(BAD_CREDENTIALS_RESPONSE);
    }

    @Test
    void loginWhenDisabled() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(USER2)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Assertions.assertNull(resultActions.andReturn().getResponse().getHeader(SecurityConstant.JWT_AUTHORIZATION_TOKEN_HEADER));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(DISABLED_RESPONSE);
    }

    @Test
    void loginWhenLocked() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(USER3)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Assertions.assertNull(resultActions.andReturn().getResponse().getHeader(SecurityConstant.JWT_AUTHORIZATION_TOKEN_HEADER));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(LOCKED_RESPONSE);
    }

    @Test
    void loginWhenBadCredentialsPassedFiveTimes() throws Exception {
        UserTo userTo = getUserToForLogin();
        userTo.setPassword("12345678");
        for (int i = 0; i < 5; i++) {
            perform(MockMvcRequestBuilders.post("/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(userTo)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        }
        ResultActions resultActions = perform(MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(userTo)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Assertions.assertNull(resultActions.andReturn().getResponse().getHeader(SecurityConstant.JWT_AUTHORIZATION_TOKEN_HEADER));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(LOCKED_RESPONSE);
        loginAttemptService.evictUserFromLoginAttemptCache(USER1.getEmail());
    }

    @Test
    void getUser() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get("/users/1000").headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        User user = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), User.class);
        assertThat(user).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "password").isEqualTo(USER1);

    }

    @Test
    void getUserWhenUnAuth() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get("/users/1000"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void getUserNotOwnWhenUserTry() throws Exception {
        perform(MockMvcRequestBuilders.get("/users/1001").headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void getUserNotOwnWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get("/users/1000").headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        User user = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), User.class);
        assertThat(user).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "password").isEqualTo(USER1);
    }

    @Test
    void getUserNotFoundWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get("/users/10").headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(USER_NOT_FOUND_RESPONSE);
    }

    @Test
    void getUserWhenNotFound() throws Exception {
        userService.deleteUser(USER1_ID);
        perform(MockMvcRequestBuilders.get("/users/1000").headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void getUsersWhenUserTry() throws Exception {
        perform(MockMvcRequestBuilders.get("/users").headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void getUsersWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get("/users").headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<User> users = JsonUtil.readValuesFromPage(resultActions.andReturn().getResponse().getContentAsString(), User.class);
        assertThat(users).usingDefaultElementComparator().isEqualTo(List.of(ADMIN, USER1, USER2, USER3));
    }

    @Test
    void getUsersWhenUnAuth() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison().ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);

    }

    @Test
    void createUser() throws Exception {
        UserTo newUserTo = getNewUserTo();
        User newUser = getNewUser();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newUserTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        User createdUser = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), User.class);
        Long newId = createdUser.getId();
        newUser.setId(newId);
        assertThat(createdUser).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "password").isEqualTo(newUser);
    }

    @Test
    void createUserWithNotValidAttributes() throws Exception {
        UserTo newUserTo = getNewUserTo();
        newUserTo.setName(null);
        ResultActions resultActions = getResultActions(newUserTo, "/users");
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_BLANK_USER_NAME_RESPONSE);

        newUserTo.setName("x");
        resultActions = getResultActions(newUserTo, "/users");
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_SIZE_USER_NAME_RESPONSE);

        newUserTo = getNewUserTo();
        newUserTo.setPassword(null);
        resultActions = getResultActions(newUserTo, "/users");
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_BLANK_USER_PASSWORD_RESPONSE);

        newUserTo.setPassword("1234");
        resultActions = getResultActions(newUserTo, "/users");
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_SIZE_USER_PASSWORD_RESPONSE);

        newUserTo = getNewUserTo();
        newUserTo.setEmail(null);
        resultActions = getResultActions(newUserTo, "/users");
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_BLANK_USER_EMAIL_RESPONSE);

        newUserTo.setEmail("dfsdf.dfsdfsd");
        resultActions = getResultActions(newUserTo, "/users");
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_PATTERN_USER_EMAIL_RESPONSE);

        newUserTo = getNewUserTo();
        newUserTo.setName(null);
        newUserTo.setPassword(null);
        resultActions = getResultActions(newUserTo, "/users");
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        Assertions.assertTrue(myHttpResponse.getMessage().contains("NAME MUST NOT BE BLANK"));
        Assertions.assertTrue(myHttpResponse.getMessage().contains("PASSWORD MUST NOT BE BLANK"));

        newUserTo = getNewUserTo();
        newUserTo.setId(2000L);
        resultActions = getResultActions(newUserTo, "/users");
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(USER_MUST_BE_NEW_RESPONSE);

    }

    @Test
    void updateUser() throws Exception {
        UserTo updatedUserTo = getUpdatedUserTo();
        User updatedUserExpected = getUpdatedUser();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put("/users/1000").headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedUserTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        User updatedUser = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), User.class);
        assertThat(updatedUser).usingRecursiveComparison()
            .ignoringFields("registered", "emailVerificationToken", "password").isEqualTo(updatedUserExpected);
    }

    @Test
    void updateUserWhenUnAuth() throws Exception {
        UserTo updatedUserTo = getUpdatedUserTo();
        perform(MockMvcRequestBuilders.put("/users/1000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedUserTo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateUserNotOwnWhenUserTry() throws Exception {
        UserTo updatedUserTo = getUpdatedUserTo();
        perform(MockMvcRequestBuilders.put("/users/1001").headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedUserTo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateUserNotOwnWhenAdminTry() throws Exception {
        UserTo updatedUserTo = getUpdatedUserTo();
        User updatedUserExpected = getUpdatedUser();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put("/users/1000").headers(adminJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedUserTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        User updatedUser = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), User.class);
        assertThat(updatedUser).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "password").isEqualTo(updatedUserExpected);
    }

    @Test
    void updateUserNotFoundWhenAdminTry() throws Exception {
        UserTo updatedUserTo = getUpdatedUserTo();
        updatedUserTo.setId(NOT_FOUND_USER_ID);
        ResultActions resultActions = perform(MockMvcRequestBuilders.put("/users/10").headers(adminJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedUserTo)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(USER_NOT_FOUND_RESPONSE);
    }

    @Test
    void updateUserWhenIdsNotTheSame() throws Exception {
        UserTo updatedUserTo = getUpdatedUserTo();
        updatedUserTo.setId(USER2_ID);
        ResultActions resultActions = perform(MockMvcRequestBuilders.put("/users/1000").headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedUserTo)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse response = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(USER_MUST_BE_WITH_ID_RESPONSE);
    }

    @Test
    void updateUserWhenIdNotPresentedInUri() throws Exception {
        UserTo updatedUserTo = getUpdatedUserTo();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put("/users").headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedUserTo)))
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(USER_UPDATE_METHOD_NOT_ALLOWED_RESPONSE);
    }

    @Test
    void deleteUser() throws Exception {
        perform(MockMvcRequestBuilders.delete("/users/1000").headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> userService.getUserById(USER1_ID));
    }

    @Test
    void deleteUserWhenUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete("/users/1000"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void deleteUserWhenNotOwnUserTry() throws Exception {
        perform(MockMvcRequestBuilders.delete("/users/1001").headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void deleteUserWhenNotOwnAdminTry() throws Exception {
        perform(MockMvcRequestBuilders.delete("/users/1000").headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> userService.getUserById(USER1_ID));
    }

    @Test
    void deleteUserWhenNotFoundAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.delete("/users/10").headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(USER_NOT_FOUND_RESPONSE);
    }

    @Test
    void enableUserWhenAdminTry() throws Exception {
        perform(MockMvcRequestBuilders.patch("/users/1000").param("enabled", "false").headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        User user = userService.getUserById(USER1_ID);
        Assertions.assertFalse(user.isEnabled());
        perform(MockMvcRequestBuilders.patch("/users/1000").param("enabled", "true").headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        user = userService.getUserById(USER1_ID);
        Assertions.assertTrue(user.isEnabled());
    }

    @Test
    void enableUserWhenUserTry() throws Exception {
        perform(MockMvcRequestBuilders.patch("/users/1000").param("enabled", "false").headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void enableUserNotFoundWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.patch("/users/10").param("enabled", "false").headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(USER_NOT_FOUND_RESPONSE);
    }

    @Test
    void verifyEmailToken() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get("/users/email-verification").param("token", USER2_EMAIL_VERIFICATION_TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EMAIL_VERIFIED_RESPONSE);
    }

    @Test
    void verifyEmailTokenWhenTokenNotPresentedInParam() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get("/users/email-verification"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(BAD_REQUEST_DATA_RESPONSE);
    }

    @Test
    void verifyEmailTokenWhenTokenIsExpired() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get("/users/email-verification").param("token", USER2_EMAIL_EXPIRED_VERIFICATION_TOKEN))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(TOKEN_EXPIRED_RESPONSE);
    }

    @Test
    void verifyEmailTokenWhenTokenIsNotValid() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get("/users/email-verification").param("token", USER2_EMAIL_NOT_VALID_VERIFICATION_TOKEN))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(BAD_REQUEST_DATA_RESPONSE);
    }

    @Test
    void requestPasswordReset() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get("/users/password-reset-request/user1@test.ru"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(PASSWORD_RESET_REQUEST_RESPONSE);
    }

    @Test
    void requestPasswordResetWhenEmailNotFound() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get("/users/password-reset-request/userXXX@test.ru"))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(PASSWORD_RESET_REQUEST_EMAIL_NOT_FOUND_RESPONSE);
    }

    @Test
    void resetPassword() throws Exception {
        userService.requestPasswordReset(USER1.getEmail());
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUser_Email(USER1.getEmail()).orElseThrow(() -> new MyEntityNotFoundException("Not found password reset token for email: " + USER1.getEmail()));
        PasswordResetModel passwordResetModel = new PasswordResetModel();
        passwordResetModel.setToken(passwordResetToken.getToken());
        passwordResetModel.setPassword("12345678");
        ResultActions resultActions = perform(MockMvcRequestBuilders.post("/users/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(passwordResetModel)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(PASSWORD_RESET_SUCCESS_RESPONSE);
        User user = userService.getUserByEmail(USER1.getEmail());
        Assertions.assertTrue(BCrypt.checkpw("12345678", user.getPassword()));
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> passwordResetTokenRepository.findByUser_Email(USER1.getEmail()).orElseThrow(() -> new MyEntityNotFoundException("Not found token")));
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> passwordResetTokenRepository.findByToken(passwordResetToken.getToken()).orElseThrow(() -> new MyEntityNotFoundException("Not found token")));
    }

    @Test
    void resetPasswordWhenTokenIsExpired() throws Exception {
        PasswordResetModel passwordResetModel = new PasswordResetModel();
        passwordResetModel.setToken(USER2_EMAIL_EXPIRED_VERIFICATION_TOKEN);
        passwordResetModel.setPassword("12345678");
        ResultActions resultActions = perform(MockMvcRequestBuilders.post("/users/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(passwordResetModel)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(TOKEN_EXPIRED_RESPONSE);
    }

    @Test
    void resetPasswordWhenTokenNotValid() throws Exception {
        PasswordResetModel passwordResetModel = new PasswordResetModel();
        passwordResetModel.setToken(USER2_EMAIL_NOT_VALID_VERIFICATION_TOKEN);
        passwordResetModel.setPassword("12345678");
        ResultActions resultActions = perform(MockMvcRequestBuilders.post("/users/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(passwordResetModel)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(BAD_REQUEST_DATA_RESPONSE);
    }

    @Test
    void resetPasswordWhenPasswordResetModelNotValid() throws Exception {
        PasswordResetModel passwordResetModel = new PasswordResetModel();
        passwordResetModel.setToken(null);
        passwordResetModel.setPassword("12345678");
        ResultActions resultActions = getResultActions(passwordResetModel, "/users/resetPassword");
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(PASSWORD_RESET_MODEL_BLANK_TOKEN_RESPONSE);
        passwordResetModel.setToken(USER2_EMAIL_VERIFICATION_TOKEN);
        passwordResetModel.setPassword(null);
        resultActions = getResultActions(passwordResetModel, "/users/resetPassword");
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(PASSWORD_RESET_MODEL_BLANK_PASSWORD_RESPONSE);
        passwordResetModel.setPassword("1234");
        resultActions = getResultActions(passwordResetModel, "/users/resetPassword");
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(PASSWORD_RESET_MODEL_NOT_VALID_PASSWORD_SIZE_RESPONSE);
    }

    @Test
    void resetPasswordWhenPasswordResetModelNotPresented() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.post("/users/resetPassword"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(BAD_REQUEST_DATA_RESPONSE);
    }

    private ResultActions getResultActions(Object to, String urlTemplate) throws Exception {
        return perform(MockMvcRequestBuilders.post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(to)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void sdfsdds() {
        String dsfsdKKDKFKDSFK123123123fdsfsdfFDFSDFFsd323242 = JWT.create()
                .withIssuer(SecurityConstant.TRAINING_MANAGEMENT_LLC)
                .withAudience(SecurityConstant.TRAINING_MANAGEMENT_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withSubject(USER2.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() - SecurityConstant.EMAIL_VERIFICATION_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512("dsfsdKKDKFKDSFK123123123fdsfsdfFDFSDFFsd323242"));
        System.out.println();

    }


}