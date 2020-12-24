package com.igar15.training_management.service.impl;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.igar15.training_management.AbstractServiceTest;
import com.igar15.training_management.constants.FileConstant;
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
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;


import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static com.igar15.training_management.testdata.UserTestData.*;
import static org.assertj.core.api.Assertions.*;

class UserServiceImplTest extends AbstractServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;


    @Test
    void getUsers() {
        Page<User> users1 = userService.getUsers(PAGEABLE_PAGE0_SIZE5);
        Page<User> users2 = userService.getUsers(PAGEABLE_PAGE1_SIZE2);
        assertThat(users1)
                .usingElementComparatorIgnoringFields("registered", "emailVerificationToken")
                .isEqualTo(PAGEO_SIZE5);
        assertThat(users2)
                .usingElementComparatorIgnoringFields("registered", "emailVerificationToken")
                .isEqualTo(PAGE1_SIZE2);
    }


    @Test
    void getUserById() {
        User user = userService.getUserById(USER1_ID);
        assertThat(user).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken").isEqualTo(USER1);
    }

    @Test
    void getUserByIdWhereNotFoundExpected() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> userService.getUserById(NOT_FOUND_USER_ID));
    }

    @Test
    void getUserByEmail() {
        User user = userService.getUserByEmail(ADMIN_EMAIL);
        assertThat(user).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken").isEqualTo(ADMIN);
    }

    @Test
    void getUserByEmailWhenNotFoundExpected() {
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
                .ignoringFields("registered", "emailVerificationToken", "password").isEqualTo(newUser);
        assertThat(userService.getUserById(createdId)).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "password").isEqualTo(newUser);
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
    }

    @Test
    void updateUser() {
        UserTo updatedUserTo = getUpdatedUserTo();
        User updatedUserExpected = getUpdatedUser();
        userService.updateUser(USER1_ID, updatedUserTo);
        assertThat(userService.getUserById(USER1_ID)).usingRecursiveComparison()
                .ignoringFields("registered", "emailVerificationToken", "password").isEqualTo(updatedUserExpected);
    }

    @Test
    void deleteUser() {
        userService.deleteUser(USER1_ID);
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> userService.getUserById(USER1_ID));
    }

    @Test
    void deleteUserWhenNotFoundExpected() {
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
    void verifyEmailTokenWhenNotFoundExpected() {
        String random = RandomString.make();
        Assertions.assertThrows(JWTDecodeException.class, () -> userService.verifyEmailToken(random));
    }

    @Test
    void verifyEmailTokenWhenTokenExpiredExpected() {
        String token = USER2_EXPIRED_VERIFICATION_TOKEN;
        User user = userService.getUserById(USER2_ID);
        user.setEmailVerificationToken(token);
        userRepository.save(user);
        Assertions.assertThrows(TokenExpiredException.class, () -> userService.verifyEmailToken(token));
    }

    @Test
    void requestPasswordResetWhenNotFoundExpected() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> userService.requestPasswordReset(NOT_FOUND_EMAIL));
    }

    @Test
    void resetPasswordWhenTokenExpiredExpected() {
        String token = USER2_EXPIRED_VERIFICATION_TOKEN;
        Assertions.assertThrows(TokenExpiredException.class, () -> userService.resetPassword(token, "123456"));
    }

    @Test
    void requestPasswordResetAndResetPassword() {
        userService.requestPasswordReset(USER1.getEmail());
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUser_Email(USER1.getEmail()).orElseThrow(() -> new MyEntityNotFoundException("Not found token with user email: " + USER1.getEmail()));
        userService.resetPassword(passwordResetToken.getToken(), "newpassword");
        User user = userService.getUserById(USER1_ID);
        Assertions.assertTrue(BCrypt.checkpw("newpassword", user.getPassword()));
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> passwordResetTokenRepository.findByUser_Email(USER1.getEmail()).orElseThrow(() -> new MyEntityNotFoundException("Not found token with user email: " + USER1.getEmail())));
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> passwordResetTokenRepository.findByToken(passwordResetToken.getToken()).orElseThrow(() -> new MyEntityNotFoundException("Not found token :" + passwordResetToken.getToken())));
    }

    @Test
    void enable() {
        Assertions.assertTrue(userService.getUserById(USER1_ID).isEnabled());
        userService.enable(USER1_ID, false);
        Assertions.assertFalse(userService.getUserById(USER1_ID).isEnabled());
    }

    @Test
    void updateProfileImageWhenUserProfileImageNotExist() throws IOException {
        Path userFolder = Paths.get(FileConstant.USER_PROFILE_IMAGE_FOLDER + USER1.getEmail()).toAbsolutePath().normalize();
        if (Files.exists(userFolder)) {
            Files.walkFileTree(userFolder, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.deleteIfExists(file);
                    return super.visitFile(file, attrs);
                }
            });
            Files.delete(userFolder);
        }
        Assertions.assertFalse(Files.exists(userFolder));
        userService.updateProfileImage(USER1_ID, PROFILE_IMAGE);
        Assertions.assertTrue(Files.exists(userFolder.resolve(USER1.getEmail() + ".jpg")));
    }

    @Test
    void updateProfileImageWhenUserProfileImageExist() throws IOException {
        Path userFolder = Paths.get(FileConstant.USER_PROFILE_IMAGE_FOLDER + USER1.getEmail()).toAbsolutePath().normalize();
        userService.updateProfileImage(USER1_ID, PROFILE_IMAGE);
        userService.updateProfileImage(USER1_ID, PROFILE_IMAGE);
        Assertions.assertTrue(Files.exists(userFolder.resolve(USER1.getEmail() + ".jpg")));
    }

}