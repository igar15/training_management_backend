package com.igar15.training_management.service;

import com.igar15.training_management.entity.User;
import com.igar15.training_management.to.UserTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<User> getUsers(Pageable pageable);

    User getUserById(long id);

    User getUserByEmail(String email);

    User createUser(UserTo userTo);

    User updateUser(long id, UserTo userTo);

    void deleteUser(long id);

    void verifyEmailToken(String token);

    void requestPasswordReset(String email);

    void resetPassword(String token, String password);

    void enable(long id, boolean enabled);

}
