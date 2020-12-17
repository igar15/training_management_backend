package com.igar15.training_management.controller;

import com.igar15.training_management.AbstractControllerTest;
import com.igar15.training_management.constants.SecurityConstant;
import com.igar15.training_management.entity.PasswordResetToken;
import com.igar15.training_management.entity.User;
import com.igar15.training_management.exceptions.MyEntityNotFoundException;
import com.igar15.training_management.repository.PasswordResetTokenRepository;
import com.igar15.training_management.repository.UserRepository;
import com.igar15.training_management.service.LoginAttemptService;
import com.igar15.training_management.service.UserService;
import com.igar15.training_management.to.MyHttpResponse;
import com.igar15.training_management.to.PasswordResetModel;
import com.igar15.training_management.to.UserTo;
import com.igar15.training_management.utils.JsonUtil;
import com.igar15.training_management.utils.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.igar15.training_management.ControllerTestData.*;
import static com.igar15.training_management.UserTestData.*;
import static org.assertj.core.api.Assertions.*;

class UserControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private LoginAttemptService loginAttemptService;


    @Test
    void login() throws Exception {
        UserTo loginUserTo = getLoginUserTo();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(loginUserTo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Assertions.assertNotNull(resultActions.andReturn().getResponse().getHeader(SecurityConstant.JWT_AUTHORIZATION_TOKEN_HEADER));
        User user = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), User.class);
        assertThat(user).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "password").isEqualTo(USER1);
    }

    @Test
    void loginWhenBadCredentialsPassed() throws Exception {
        UserTo loginUserTo = getLoginUserTo();
        loginUserTo.setPassword("12345678");
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(loginUserTo)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Assertions.assertNull(resultActions.andReturn().getResponse().getHeader(SecurityConstant.JWT_AUTHORIZATION_TOKEN_HEADER));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(BAD_CREDENTIALS_RESPONSE);
    }

    @Test
    void loginWhenUserDisabled() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/login")
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
    void loginWhenUserLocked() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/login")
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
        UserTo loginUserTo = getLoginUserTo();
        loginUserTo.setPassword("12345678");
        for (int i = 0; i < 5; i++) {
            perform(MockMvcRequestBuilders.post(USERS_URI + "/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(loginUserTo)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        }
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(loginUserTo)))
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
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        User user = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), User.class);
        assertThat(user).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "password").isEqualTo(USER1);

    }

    @Test
    void getUserWhenUnAuth() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void getUserNotOwnWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER2_ID).headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void getUserNotOwnWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID).headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        User user = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), User.class);
        assertThat(user).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "password").isEqualTo(USER1);
    }

    @Test
    void getUserNotFoundWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + NOT_FOUND_USER_ID).headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(USER_NOT_FOUND_RESPONSE);
    }

    @Test
    void getUserWhenNotFound() throws Exception {
        userService.deleteUser(USER1_ID);
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/" + USER1_ID).headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void getAllUsersWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI).headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void getAllUsersWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI).headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<User> users = JsonUtil.readValuesFromPage(resultActions.andReturn().getResponse().getContentAsString(), User.class);
        assertThat(users).usingDefaultElementComparator().isEqualTo(List.of(ADMIN, USER1, USER2, USER3));
    }

    @Test
    void getAllUsersWhenUnAuth() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison().ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);

    }

    @Test
    void createUser() throws Exception {
        UserTo newUserTo = getNewUserTo();
        User newUser = getNewUser();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI)
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
        newUserTo.setEmail(USER1.getEmail());
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newUserTo)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EMAIL_ALREADY_EXIST_RESPONSE);

        newUserTo.setName(null);
        resultActions = getResultActions(newUserTo, USERS_URI);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_USER_NAME_BLANK_RESPONSE);

        newUserTo.setName("x");
        resultActions = getResultActions(newUserTo, USERS_URI);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_USER_NAME_SIZE_RESPONSE);

        newUserTo = getNewUserTo();
        newUserTo.setPassword(null);
        resultActions = getResultActions(newUserTo, USERS_URI);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_USER_PASSWORD_BLANK_RESPONSE);

        newUserTo.setPassword("1234");
        resultActions = getResultActions(newUserTo, USERS_URI);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_USER_PASSWORD_SIZE_RESPONSE);

        newUserTo = getNewUserTo();
        newUserTo.setEmail(null);
        resultActions = getResultActions(newUserTo, USERS_URI);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_USER_EMAIL_BLANK_RESPONSE);

        newUserTo.setEmail("xxxx.xxxxxx");
        resultActions = getResultActions(newUserTo, USERS_URI);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(NOT_VALID_USER_EMAIL_PATTERN_RESPONSE);

        newUserTo = getNewUserTo();
        newUserTo.setName(null);
        newUserTo.setPassword(null);
        resultActions = getResultActions(newUserTo, USERS_URI);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        Assertions.assertTrue(myHttpResponse.getMessage().contains("NAME MUST NOT BE BLANK"));
        Assertions.assertTrue(myHttpResponse.getMessage().contains("PASSWORD MUST NOT BE BLANK"));

        newUserTo = getNewUserTo();
        newUserTo.setId(100L);
        resultActions = getResultActions(newUserTo, USERS_URI);
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(USER_MUST_BE_NEW_RESPONSE);
    }

    @Test
    void updateUser() throws Exception {
        UserTo updatedUserTo = getUpdatedUserTo();
        User updatedUserExpected = getUpdatedUser();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID)
                .headers(userJwtHeader)
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
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedUserTo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison().ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void updateUserNotOwnWhenUserTry() throws Exception {
        UserTo updatedUserTo = getUpdatedUserTo();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER2_ID)
                .headers(userJwtHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedUserTo)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison().ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void updateUserNotOwnWhenAdminTry() throws Exception {
        UserTo updatedUserTo = getUpdatedUserTo();
        User updatedUserExpected = getUpdatedUser();
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID)
                .headers(adminJwtHeader)
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
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + NOT_FOUND_USER_ID)
                .headers(adminJwtHeader)
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
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI + "/" + USER1_ID)
                .headers(userJwtHeader)
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
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(USERS_URI)
                .headers(userJwtHeader)
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
        perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER1_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> userService.getUserById(USER1_ID));
    }

    @Test
    void deleteUserWhenUnAuth() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER1_ID))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison().ignoringFields("timeStamp").isEqualTo(FORBIDDEN_RESPONSE);
    }

    @Test
    void deleteUserWhenNotOwnUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER2_ID)
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison().ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void deleteUserWhenNotOwnAdminTry() throws Exception {
        perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + USER1_ID)
                .headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> userService.getUserById(USER1_ID));
    }

    @Test
    void deleteUserWhenNotFoundAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.delete(USERS_URI + "/" + NOT_FOUND_USER_ID)
                .headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(USER_NOT_FOUND_RESPONSE);
    }

    @Test
    void enableUserWhenAdminTry() throws Exception {
        perform(MockMvcRequestBuilders.patch(USERS_URI + "/" + USER1_ID)
                .param("enabled", "false")
                .headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        User user = userService.getUserById(USER1_ID);
        Assertions.assertFalse(user.isEnabled());
        perform(MockMvcRequestBuilders.patch(USERS_URI + "/" + USER1_ID)
                .param("enabled", "true")
                .headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        user = userService.getUserById(USER1_ID);
        Assertions.assertTrue(user.isEnabled());
    }

    @Test
    void enableUserWhenUserTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.patch(USERS_URI + "/" + USER1_ID)
                .param("enabled", "false")
                .headers(userJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison().ignoringFields("timeStamp").isEqualTo(ACCESS_DENIED_RESPONSE);
    }

    @Test
    void enableUserNotFoundWhenAdminTry() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.patch(USERS_URI + "/" + NOT_FOUND_USER_ID)
                .param("enabled", "false")
                .headers(adminJwtHeader))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(USER_NOT_FOUND_RESPONSE);
    }

    @Test
    void verifyEmailToken() throws Exception {
        String emailVerificationToken = jwtTokenProvider.generateEmailVerificationToken(USER2.getEmail());
        User user = userService.getUserById(USER2_ID);
        user.setEmailVerificationToken(emailVerificationToken);
        userRepository.save(user);
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/email-verification")
                .param("token", emailVerificationToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(EMAIL_VERIFIED_RESPONSE);
    }

    @Test
    void verifyEmailTokenWhenTokenIsNotPresentedInParam() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/email-verification"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(BAD_REQUEST_DATA_RESPONSE);
    }

    @Test
    void verifyEmailTokenWhenTokenIsExpired() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/email-verification")
                .param("token", USER2_EXPIRED_VERIFICATION_TOKEN))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(TOKEN_EXPIRED_RESPONSE);
    }

    @Test
    void verifyEmailTokenWhenTokenIsNotValid() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/email-verification")
                .param("token", USER2_NOT_VALID_VERIFICATION_TOKEN))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(BAD_REQUEST_DATA_RESPONSE);
    }

    @Test
    void requestPasswordReset() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/password-reset-request/" + USER1.getEmail()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(PASSWORD_RESET_REQUEST_RESPONSE);
    }

    @Test
    void requestPasswordResetWhenEmailNotFound() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(USERS_URI + "/password-reset-request/" + NOT_FOUND_EMAIL))
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
        passwordResetModel.setPassword("newpassword");
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(passwordResetModel)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(PASSWORD_RESET_SUCCESS_RESPONSE);
        User user = userService.getUserByEmail(USER1.getEmail());
        Assertions.assertTrue(BCrypt.checkpw("newpassword", user.getPassword()));
    }

    @Test
    void resetPasswordWhenTokenIsExpired() throws Exception {
        PasswordResetModel passwordResetModel = new PasswordResetModel();
        passwordResetModel.setToken(USER2_EXPIRED_VERIFICATION_TOKEN);
        passwordResetModel.setPassword("newpassword");
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(passwordResetModel)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(TOKEN_EXPIRED_RESPONSE);
    }

    @Test
    void resetPasswordWhenTokenIsNotValid() throws Exception {
        PasswordResetModel passwordResetModel = new PasswordResetModel();
        passwordResetModel.setToken(USER2_NOT_VALID_VERIFICATION_TOKEN);
        passwordResetModel.setPassword("newpassword");
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(passwordResetModel)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(BAD_REQUEST_DATA_RESPONSE);
    }

    @Test
    void resetPasswordWhenPasswordResetModelIsNotValid() throws Exception {
        PasswordResetModel passwordResetModel = new PasswordResetModel();
        passwordResetModel.setToken(null);
        passwordResetModel.setPassword("newpassword");
        ResultActions resultActions = getResultActions(passwordResetModel, USERS_URI + "/resetPassword");
        MyHttpResponse myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(PASSWORD_RESET_MODEL_TOKEN_BLANK_RESPONSE);

        String passwordResetToken = jwtTokenProvider.generatePasswordResetToken(USER2.getEmail());
        passwordResetModel.setToken(passwordResetToken);
        passwordResetModel.setPassword(null);
        resultActions = getResultActions(passwordResetModel, USERS_URI + "/resetPassword");
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(PASSWORD_RESET_MODEL_PASSWORD_BLANK_RESPONSE);

        passwordResetModel.setPassword("1234");
        resultActions = getResultActions(passwordResetModel, USERS_URI + "/resetPassword");
        myHttpResponse = JsonUtil.readValue(resultActions.andReturn().getResponse().getContentAsString(), MyHttpResponse.class);
        assertThat(myHttpResponse).usingRecursiveComparison()
                .ignoringFields("timeStamp").isEqualTo(PASSWORD_RESET_MODEL_NOT_VALID_PASSWORD_SIZE_RESPONSE);
    }

    @Test
    void resetPasswordWhenPasswordResetModelIsNotPresented() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(USERS_URI + "/resetPassword"))
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

}