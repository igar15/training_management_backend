package com.igar15.training_management.controller;

import com.igar15.training_management.entity.User;
import com.igar15.training_management.exceptions.IllegalRequestDataException;
import com.igar15.training_management.service.UserService;
import com.igar15.training_management.to.MyHttpResponse;
import com.igar15.training_management.to.PasswordResetModel;
import com.igar15.training_management.to.UserTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


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
    public ResponseEntity<User> createUser(@RequestBody UserTo userTo) {
        if (userTo.getId() != null) {
            throw new IllegalRequestDataException(userTo + " must be new (id=null)");
        }
        log.info("create {}", userTo);
        User user = userService.createUser(userTo);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody UserTo userTo) {
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
    public ResponseEntity<MyHttpResponse> resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
        log.info("reset password for {}", passwordResetModel);
        userService.resetPassword(passwordResetModel.getToken(), passwordResetModel.getPassword());
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), "Your password was successfully reset");
        return new ResponseEntity<>(myHttpResponse, HttpStatus.OK);
    }

}
