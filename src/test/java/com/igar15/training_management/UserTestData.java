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

    public static final long NOT_FOUND_USER_ID = 10;
    public static final String NOT_FOUND_EMAIL = "user99@yandex.ru";
    public static final long USER1_ID = 1000;
    public static final long USER2_ID = 1001;
    public static final long USER3_ID = 1002;
    public static final long ADMIN_ID = 1003;
    public static final String ADMIN_EMAIL = "admin@test.ru";
//    public static final String USER2_EMAIL_VERIFICATION_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJhdWQiOiJUcmFpbmluZyBtYW5hZ2VtZW50IFBvcnRhbCIsInN1YiI6InVzZXIyQHRlc3QucnUiLCJpc3MiOiJUcmFpbmluZyBtYW5hZ2VtZW50LCBMTEMiLCJleHAiOjE2MDgxODc3ODEsImlhdCI6MTYwODEwMTM4MX0.FwiGwzHv98wwqIoFNlmYmp1oYu7kELtjUcC3JkeL85CA2SFHQ1hLFA6qYdggKOO8fHYByDVMVWHVsOIUaQFyag";
    public static final String USER2_EXPIRED_VERIFICATION_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJhdWQiOiJUcmFpbmluZyBtYW5hZ2VtZW50IFBvcnRhbCIsInN1YiI6InVzZXIyQHRlc3QucnUiLCJpc3MiOiJUcmFpbmluZyBtYW5hZ2VtZW50LCBMTEMiLCJleHAiOjE2MDgwNDg5ODcsImlhdCI6MTYwODEzNTM4N30.wTHbFC29jK8vtbzlfpfeu4VvtJsBaSp4Msz-DMKOHgtMsZ4LacMuSWJOcHO_jL0-UA9_FzHo6dhipqg7jYZOqQ";
    public static final String USER2_NOT_VALID_VERIFICATION_TOKEN = "eyJ0eXAiOiJKV1QiLCvcvcxGciOiJIUzUxMiJ9.cxdvdvfdvdvd.Np_feN7bAvZRfsRf3XzyPBC1riFdfsEs5OKJnOL9iqX-zqvL1sOxe3iWGGw608umJzcALoLzxkPK0UlHlvCx0zg";

    public static final User USER1 = new User(USER1_ID, "user1", "user1@test.ru", "$2y$10$mM4j46rI5jEwIa4FjZZUdOCD1VgO0.KovMo4ZDhcS/Tvg8v4aV4TO", true, true, Role.ROLE_USER);
    public static final User USER2 = new User(USER2_ID, "user2", "user2@test.ru", "123456", false, true, Role.ROLE_USER);
    public static final User USER3 = new User(USER3_ID, "user3", "user3@test.ru", "123456", true, false, Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "admin", "admin@test.ru", "$2y$10$mM4j46rI5jEwIa4FjZZUdOCD1VgO0.KovMo4ZDhcS/Tvg8v4aV4TO", true, true, Role.ROLE_ADMIN);

    public static final Pageable PAGEABLE_PAGE0_SIZE5 = PageRequest.of(0, 5);
    public static final Pageable PAGEABLE_PAGE1_SIZE2 = PageRequest.of(1, 2);
    public static final Page<User> PAGEO_SIZE5 = new PageImpl<>(List.of(USER1, USER2, USER3, ADMIN), PAGEABLE_PAGE0_SIZE5, 4);
    public static final Page<User> PAGE1_SIZE2 = new PageImpl<>(List.of(USER3, ADMIN), PAGEABLE_PAGE1_SIZE2, 4);

    public static UserTo getNewUserTo() {
        return new UserTo(null, "new user", "newemail@test.com", "newpassword");
    }

    public static User getNewUser() {
        return new User(null, "new user", "newemail@test.com", "newpassword", Role.ROLE_USER);
    }

    public static UserTo getUpdatedUserTo() {
        return new UserTo(USER1_ID, "user1 updated", "user1@test.ru updated", "123456 updated");
    }

    public static User getUpdatedUser() {
        return new User(USER1_ID, "user1 updated", "user1@test.ru", "123456", true, true, Role.ROLE_USER);
    }

    public static UserTo getLoginUserTo() {
        UserTo userTo = new UserTo();
        userTo.setEmail(USER1.getEmail());
        userTo.setPassword("123456");
        return userTo;
    }

}
