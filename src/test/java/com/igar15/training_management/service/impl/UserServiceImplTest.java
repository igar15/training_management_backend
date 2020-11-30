package com.igar15.training_management.service.impl;

import com.igar15.training_management.AbstractServiceTest;
import com.igar15.training_management.UserTestData;
import com.igar15.training_management.entity.User;
import com.igar15.training_management.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import static com.igar15.training_management.UserTestData.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest extends AbstractServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void getUsers() {
        Page<User> users1 = userService.getUsers(PAGEABLE_PAGE0_SIZE5);
        Page<User> users2 = userService.getUsers(PAGEABLE_PAGE1_SIZE2);
        Assertions.assertEquals(PAGEO_SIZE5, users1);
        Assertions.assertEquals(PAGE1_SIZE2, users2);
    }


    @Test
    void getUserById() {
    }

    @Test
    void getUserByEmail() {
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
    void verifyEmailToken() {
    }

    @Test
    void requestPasswordReset() {
    }

    @Test
    void resetPassword() {
    }
}