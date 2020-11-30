package com.igar15.training_management.service.impl;

import com.igar15.training_management.entity.User;
import com.igar15.training_management.entity.enums.Role;
import com.igar15.training_management.exceptions.EmailExistException;
import com.igar15.training_management.exceptions.UserNotFoundException;
import com.igar15.training_management.repository.UserRepository;
import com.igar15.training_management.service.EmailService;
import com.igar15.training_management.service.UserService;
import com.igar15.training_management.to.UserTo;
import com.igar15.training_management.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;



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
    public User createUser(UserTo userTo) {
        Assert.notNull(userTo, "User must not be null");
        if (userRepository.findByEmail(userTo.getEmail()).isPresent()) {
            throw new EmailExistException("User with email " + userTo.getEmail() + " already exists");
        }
        userTo.setEmail(userTo.getEmail().toLowerCase());
        // need to encrypt password
        User user = new User();
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail());
        user.setPassword(userTo.getPassword());
        user.setEmailVerificationToken(jwtTokenProvider.generateEmailVerificationToken(user.getEmail()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        emailService.sendEmailAddressVerificationEmail(user.getName(), user.getEmail(), user.getEmailVerificationToken());
        return user;
    }

    @Override
    public User updateUser(long id, UserTo userTo) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Not found user with id: " + id));
        user.setName(userTo.getName());
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Not found user with id: " + id));
        userRepository.delete(user);
    }

    @Override
    public boolean verifyEmailToken(String token) {
        boolean returnValue = false;
        Optional<User> userOptional = userRepository.findByEmailVerificationToken(token);
        if (userOptional.isPresent()) {
            if (!jwtTokenProvider.isTokenExpired(token)) {
                User user = userOptional.get();
                user.setEmailVerificationToken(null);
                user.setEmailVerificationStatus(true);
                userRepository.save(user);
                returnValue = true;
            }
        }
        return returnValue;
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
