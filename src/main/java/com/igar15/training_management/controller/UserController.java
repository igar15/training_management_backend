package com.igar15.training_management.controller;

import com.igar15.training_management.constants.SecurityConstant;
import com.igar15.training_management.entity.User;
import com.igar15.training_management.exceptions.IllegalRequestDataException;
import com.igar15.training_management.security.UserPrincipal;
import com.igar15.training_management.service.UserService;
import com.igar15.training_management.to.MyHttpResponse;
import com.igar15.training_management.to.PasswordResetModel;
import com.igar15.training_management.to.UserTo;
import com.igar15.training_management.utils.JwtTokenProvider;
import com.igar15.training_management.utils.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;



    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserTo userTo) {
        log.info("login user email={}", userTo.getEmail());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userTo.getEmail(), userTo.getPassword()));
        } catch (DisabledException e) {
            throw new DisabledException("User " + userTo.getEmail() + " disabled");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("User " + userTo.getEmail() + " bad credentials");
        } catch (LockedException e) {
            throw new LockedException("User " + userTo.getEmail() + " account is locked");
        }
        User loginUser = userService.getUserByEmail(userTo.getEmail());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = new HttpHeaders();
        jwtHeader.add(SecurityConstant.JWT_AUTHORIZATION_TOKEN_HEADER, jwtTokenProvider.generateAuthorizationToken(userPrincipal));
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        log.info("get user id={}", id);
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<User>> getUsers(@SortDefault("email") Pageable pageable) {
        log.info("get all users with pagination ( {} )", pageable);
        Page<User> users = userService.getUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserTo userTo, BindingResult bindingResult) {
        ValidationUtil.validateTo(bindingResult);
        if (userTo.getId() != null) {
            throw new IllegalRequestDataException(userTo + " must be new (id=null)");
        }
        log.info("create {}", userTo);
        User user = userService.createUser(userTo);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserTo userTo, BindingResult bindingResult) {
        ValidationUtil.validateTo(bindingResult, "password");
        if (!id.equals(userTo.getId())) {
            throw new IllegalRequestDataException(userTo + " must be with id=" + id);
        }
        log.info("update {} with id={}", userTo, id);
        User user = userService.updateUser(id, userTo);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") long id) {
        log.info("delete user id={}", id);
        userService.deleteUser(id);
    }

    @GetMapping("/email-verification")
    public ResponseEntity<MyHttpResponse> verifyEmailToken(@RequestParam("token") String token) {
        log.info("verify email with token={}", token);
        userService.verifyEmailToken(token);
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), "Your email was successfully verified.");
        return new ResponseEntity<>(myHttpResponse, HttpStatus.OK);
    }

    @GetMapping("/password-reset-request/{email}")
    public ResponseEntity<MyHttpResponse> requestPasswordReset(@PathVariable("email") String email) {
        log.info("request password reset for email={}", email);
        userService.requestPasswordReset(email);
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), "The message with link to reset your password was sent to " + email);
        return new ResponseEntity<>(myHttpResponse, HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<MyHttpResponse> resetPassword(@Valid @RequestBody PasswordResetModel passwordResetModel, BindingResult bindingResult) {
        ValidationUtil.validateTo(bindingResult);
        log.info("reset password for {}", passwordResetModel);
        userService.resetPassword(passwordResetModel.getToken(), passwordResetModel.getPassword());
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), "Your password was successfully reset");
        return new ResponseEntity<>(myHttpResponse, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableUser(@PathVariable("id") long id, @RequestParam("enabled") boolean enabled) {
        log.info(enabled ? "enable user id={}" : "disable user id={}", id);
        userService.enable(id, enabled);
    }

}
