package com.igar15.training_management.service.impl;

import com.igar15.training_management.entity.PasswordResetToken;
import com.igar15.training_management.entity.User;
import com.igar15.training_management.entity.enums.Role;
import com.igar15.training_management.exceptions.EmailExistException;
import com.igar15.training_management.exceptions.MyEntityNotFoundException;
import com.igar15.training_management.repository.PasswordResetTokenRepository;
import com.igar15.training_management.repository.UserRepository;
import com.igar15.training_management.security.UserPrincipal;
import com.igar15.training_management.service.EmailService;
import com.igar15.training_management.service.LoginAttemptService;
import com.igar15.training_management.service.UserService;
import com.igar15.training_management.to.UserTo;
import com.igar15.training_management.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private LoginAttemptService loginAttemptService;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("Not found user with email: " + email);
        }
        else {
            User user = optionalUser.get();
            validateLoginAttempt(user);
            return new UserPrincipal(user);
        }
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Not found user with id: " + id));
    }

    @Override
    public User getUserByEmail(String email) {
        Assert.notNull(email, "Email must not be null");
        return userRepository.findByEmail(email).orElseThrow(() -> new MyEntityNotFoundException("Not found user with email: " + email));
    }

    @Override
    @Transactional
    public User createUser(UserTo userTo) {
        Assert.notNull(userTo, "User must not be null");
        if (userRepository.findByEmail(userTo.getEmail()).isPresent()) {
            throw new EmailExistException("User with email " + userTo.getEmail() + " already exists");
        }
        String email = userTo.getEmail();
        if (email != null) {
            userTo.setEmail(email.toLowerCase());
        }
        User user = new User();
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userTo.getPassword()));
        user.setEmailVerificationToken(jwtTokenProvider.generateEmailVerificationToken(user.getEmail()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        emailService.sendEmailAddressVerificationEmail(user.getName(), user.getEmail(), user.getEmailVerificationToken());
        return user;
    }

    @Override
    @Transactional
    public User updateUser(long id, UserTo userTo) {
        Assert.notNull(userTo, "User must not be null");
        User user = userRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Not found user with id: " + id));
        user.setName(userTo.getName());
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUser(long id) {
        User user = getUserById(id);
        userRepository.delete(user);
        loginAttemptService.evictUserFromLoginAttemptCache(user.getEmail());
    }

    @Override
    @Transactional
    public void verifyEmailToken(String token) {
        Assert.notNull(token, "Email verification token must not be null");
        User user = userRepository.findByEmailVerificationToken(token).orElseThrow(() -> new MyEntityNotFoundException("Not found user with such token"));
        jwtTokenProvider.isTokenExpired(token);
        user.setEmailVerificationToken(null);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        Assert.notNull(email, "Email must not be null");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new MyEntityNotFoundException("Not found user with email: " + email));
        String token = jwtTokenProvider.generatePasswordResetToken(email);
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetTokenRepository.save(passwordResetToken);
        emailService.sendPasswordResetEmail(user.getName(), email, token);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String password) {
        jwtTokenProvider.isTokenExpired(token);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new MyEntityNotFoundException("Not found token with token: " + token));
        User user = passwordResetToken.getUser();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
    }

    @Override
    @Transactional
    public void enable(long id, boolean enabled) {
        User user = getUserById(id);
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    private void validateLoginAttempt(User user) {
        if (user.getIsNonLocked()) {
            if (loginAttemptService.hasExceededMaxAttempts(user.getEmail())) {
                user.setIsNonLocked(false);
                userRepository.save(user);
            }
        }
        else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getEmail());
        }
    }
}
