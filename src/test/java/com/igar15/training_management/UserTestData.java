package com.igar15.training_management;

import com.igar15.training_management.entity.User;
import com.igar15.training_management.entity.enums.Role;
import com.igar15.training_management.to.UserTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;


public class UserTestData {

    public static final long NOT_FOUND_ID = 10;
    public static final String NOT_FOUND_EMAIL = "user99@yandex.ru";
    public static final long USER1_ID = 1000;
    public static final long USER2_ID = 1001;
    public static final long USER3_ID = 1002;
    public static final long ADMIN_ID = 1003;
    public static final String ADMIN_EMAIL = "admin@test.ru";

    public static final User USER1 = new User(USER1_ID, "user1", "user1@test.ru", "123456", Role.ROLE_USER);
    public static final User USER2 = new User(USER2_ID, "user2", "user2@test.ru", "123456", Role.ROLE_USER);
    public static final User USER3 = new User(USER3_ID, "user3", "user3@test.ru", "123456", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "admin", "admin@test.ru", "123456", Role.ROLE_ADMIN);

    public static final Pageable PAGEABLE_PAGE0_SIZE5 = PageRequest.of(0, 5);
    public static final Pageable PAGEABLE_PAGE1_SIZE2 = PageRequest.of(1, 2);
    public static final Page<User> PAGEO_SIZE5 = new PageImpl<>(List.of(USER1, USER2, USER3, ADMIN), PAGEABLE_PAGE0_SIZE5, 4);
    public static final Page<User> PAGE1_SIZE2 = new PageImpl<>(List.of(USER3, ADMIN), PAGEABLE_PAGE1_SIZE2, 4);

    public static UserTo getNewUserTo() {
        return new UserTo("new user", "newemail@test.com", "newpassword");
    }

    public static User getNewUser() {
        return new User(null, "new user", "newemail@test.com", "newpassword", Role.ROLE_USER);
    }

    public static UserTo getUpdatedUserTo() {
        return new UserTo("user1 updated", "user1@test.ru updated", "123456 updated");
    }

    public static User getUpdatedUser() {
        return new User(USER1_ID, "user1 updated", "user1@test.ru", "123456", Role.ROLE_USER);
    }
}
