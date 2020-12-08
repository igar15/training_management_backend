package com.igar15.training_management.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.igar15.training_management.AbstractServiceTest;
import com.igar15.training_management.constants.SecurityConstant;
import com.igar15.training_management.entity.PasswordResetToken;
import com.igar15.training_management.entity.User;
import com.igar15.training_management.exceptions.EmailExistException;
import com.igar15.training_management.exceptions.MyEntityNotFoundException;
import com.igar15.training_management.repository.PasswordResetTokenRepository;
import com.igar15.training_management.repository.UserRepository;
import com.igar15.training_management.service.UserService;
import com.igar15.training_management.to.UserTo;
import com.igar15.training_management.utils.JwtTokenProvider;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;

import javax.validation.ConstraintViolationException;

import java.util.Date;

import static com.igar15.training_management.UserTestData.*;
import static org.assertj.core.api.Assertions.*;

class UserServiceImplTest extends AbstractServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Environment environment;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;


    @Test
    void getUsers() {
        Page<User> users1 = userService.getUsers(PAGEABLE_PAGE0_SIZE5);
        Page<User> users2 = userService.getUsers(PAGEABLE_PAGE1_SIZE2);
        assertThat(users1)
                .usingElementComparatorIgnoringFields("registered", "emailVerificationToken",
                        "enabled", "isNonLocked")
                .isEqualTo(PAGEO_SIZE5);
        assertThat(users2)
                .usingElementComparatorIgnoringFields("registered", "emailVerificationToken",
                        "enabled", "isNonLocked")
                .isEqualTo(PAGE1_SIZE2);
    }


    @Test
    void getUserById() {
        User user = userService.getUserById(USER1_ID);
        assertThat(user).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "enabled", "isNonLocked").isEqualTo(USER1);
    }

    @Test
    void getUserByIdWhereNotFoundExpected() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> userService.getUserById(NOT_FOUND_USER_ID));
    }

    @Test
    void getUserByEmail() {
        User user = userService.getUserByEmail(ADMIN_EMAIL);
        assertThat(user).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "enabled", "isNonLocked").isEqualTo(ADMIN);
    }

    @Test
    void getUserByEmailWhereNotFoundExpected() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> userService.getUserByEmail(NOT_FOUND_EMAIL));
    }

    @Test
    void createUser() {
        UserTo newUserTo = getNewUserTo();
        User newUser = getNewUser();
        User createdUser = userService.createUser(newUserTo);
        long createdId = createdUser.getId();
        newUser.setId(createdId);
        assertThat(createdUser).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "emailVerificationStatus", "isNonLocked", "password").isEqualTo(newUser);
        assertThat(userService.getUserById(createdId)).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "emailVerificationStatus", "isNonLocked", "password").isEqualTo(newUser);
    }

    @Test
    void createUserWithExistingEmail() {
        UserTo newUserTo = getNewUserTo();
        newUserTo.setEmail(USER1.getEmail());
        Assertions.assertThrows(EmailExistException.class, () -> userService.createUser(newUserTo));
    }

    @Test
    void createUserWithNotValidAttributes() {
        validateRootCause(() -> userService.createUser(new UserTo(null, null, "test@test.com", "123456")), ConstraintViolationException.class);
        validateRootCause(() -> userService.createUser(new UserTo(null, "", "test@test.com", "123456")), ConstraintViolationException.class);
        validateRootCause(() -> userService.createUser(new UserTo(null, "a", "test@test.com", "123456")), ConstraintViolationException.class);
        validateRootCause(() -> userService.createUser(new UserTo(null, "test", null, "123456")), ConstraintViolationException.class);
        validateRootCause(() -> userService.createUser(new UserTo(null, "test", "", "123456")), ConstraintViolationException.class);
        validateRootCause(() -> userService.createUser(new UserTo(null, "test", "testtest.com", "123456")), ConstraintViolationException.class);
    }

    @Test
    void updateUser() {
        UserTo updatedUserTo = getUpdatedUserTo();
        User updatedUserExpected = getUpdatedUser();
        userService.updateUser(USER1_ID, updatedUserTo);
        assertThat(userService.getUserById(USER1_ID)).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "enabled", "isNonLocked").isEqualTo(updatedUserExpected);
    }

    @Test
    void deleteUser() {
        userService.deleteUser(USER1_ID);
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> userService.getUserById(USER1_ID));
    }

    @Test
    void deleteUserWhereNotFoundExpected() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> userService.deleteUser(NOT_FOUND_USER_ID));
    }

    @Test
    void verifyEmailToken() {
        String token = jwtTokenProvider.generateEmailVerificationToken(USER2.getEmail());
        User user = userService.getUserById(USER2_ID);
        user.setEmailVerificationToken(token);
        userRepository.save(user);
        userService.verifyEmailToken(token);
        user = userService.getUserById(USER2_ID);
        Assertions.assertNull(user.getEmailVerificationToken());
        Assertions.assertTrue(user.isEnabled());
    }

    @Test
    void verifyEmailTokenWhereNotFoundExpected() {
        String random = RandomString.make();
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> userService.verifyEmailToken(random));
    }

    @Test
    void verifyEmailTokenWhereTokenExpiredExpected() {
        String token = makeExpiredToken();
        User user = userService.getUserById(USER2_ID);
        user.setEmailVerificationToken(token);
        userRepository.save(user);
        Assertions.assertThrows(TokenExpiredException.class, () -> userService.verifyEmailToken(token));
    }

    @Test
    void requestPasswordResetWhereNotFoundExpected() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> userService.requestPasswordReset(NOT_FOUND_EMAIL));
    }

    @Test
    void resetPasswordWhereTokenExpiredExpected() {
        String token = makeExpiredToken();
        Assertions.assertThrows(TokenExpiredException.class, () -> userService.resetPassword(token, "123456"));
    }

    @Test
    void requestPasswordResetAndResetPassword() {
        userService.requestPasswordReset(USER1.getEmail());
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUser_Email(USER1.getEmail()).orElseThrow(() -> new MyEntityNotFoundException("Not found token with user email: " + USER1.getEmail()));
        userService.resetPassword(passwordResetToken.getToken(), "newpassword");
        User user = userService.getUserById(USER1_ID);
//        Assertions.assertEquals("newpassword", user.getPassword());
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> passwordResetTokenRepository.findByUser_Email(USER1.getEmail()).orElseThrow(() -> new MyEntityNotFoundException("Not found token with user email: " + USER1.getEmail())));
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> passwordResetTokenRepository.findByToken(passwordResetToken.getToken()).orElseThrow(() -> new MyEntityNotFoundException("Not found token :" + passwordResetToken.getToken())));
    }

    @Test
    void enable() {
        Assertions.assertTrue(userService.getUserById(USER1_ID).isEnabled());
        userService.enable(USER1_ID, false);
        Assertions.assertFalse(userService.getUserById(USER1_ID).isEnabled());
    }



    private String makeExpiredToken() {
        return JWT.create()
                .withIssuedAt(new Date())
                .withSubject(USER2.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() - SecurityConstant.EMAIL_VERIFICATION_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(environment.getProperty("jwt.secretKey")));
    }
}