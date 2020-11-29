package com.igar15.training_management.service.impl;

import com.igar15.training_management.entity.User;
import com.igar15.training_management.exceptions.UserNotFoundException;
import com.igar15.training_management.repository.UserRepository;
import com.igar15.training_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;





    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Not found user with id: " + id));
    }

    @Override
    public User getUserByEmail(String email) {
        Assert.notNull(email, "Email must not be null");
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Not found user with email: " + email));
    }

    @Override
    public User createUser(User user) {
        Assert.notNull(user, "User must not be null");

        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(long id) {

    }

    @Override
    public boolean verifyEmailToken(String token) {
        return false;
    }

    @Override
    public boolean requestPasswordReset(String email) {
        return false;
    }

    @Override
    public boolean resetPassword(String token, String password) {
        return false;
    }
}
