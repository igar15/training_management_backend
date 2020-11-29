package com.igar15.training_management.service;

import com.igar15.training_management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<User> getUsers(Pageable pageable);

    User getUserById(long id);

    User getUserByEmail(String email);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(long id);

    boolean verifyEmailToken(String token);

    boolean requestPasswordReset(String email);

    boolean resetPassword(String token, String password);

}
