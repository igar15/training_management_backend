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
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;


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
    @ApiOperation(value = "The User Login Web Service Endpoint", notes = "${userController.Login.ApiOperation.Notes}")
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.id")
    @ApiOperation(value = "The Get User Details Web Service Endpoint", notes = "${userController.GetUser.ApiOperation.Notes}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${authorizationHeader.description}", paramType = "header")
    })
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        log.info("get user id={}", id);
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "The Get Users Details Web Service Endpoint", notes = "${userController.GetUsers.ApiOperation.Notes}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${authorizationHeader.description}", paramType = "header")
    })
    public ResponseEntity<Page<User>> getUsers(@SortDefault("email") Pageable pageable) {
        log.info("get all users with pagination ( {} )", pageable);
        Page<User> users = userService.getUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "The Create User Web Service Endpoint", notes = "${userController.CreateUser.ApiOperation.Notes}")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserTo userTo, BindingResult bindingResult) {
        ValidationUtil.validateTo(bindingResult);
        ValidationUtil.checkOnNew(userTo);
        log.info("create {}", userTo);
        User user = userService.createUser(userTo);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.id")
    @ApiOperation(value = "The Update User Web Service Endpoint", notes = "${userController.UpdateUser.ApiOperation.Notes}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${authorizationHeader.description}", paramType = "header")
    })
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserTo userTo, BindingResult bindingResult) {
        ValidationUtil.validateTo(bindingResult, "password");
        ValidationUtil.checkIdTheSame(userTo, id);
        log.info("update {} with id={}", userTo, id);
        User user = userService.updateUser(id, userTo);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "The Delete User Web Service Endpoint", notes = "${userController.DeleteUser.ApiOperation.Notes}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${authorizationHeader.description}", paramType = "header")
    })
    public void deleteUser(@PathVariable("id") long id) {
        log.info("delete user id={}", id);
        userService.deleteUser(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "The Enable User Web Service Endpoint", notes = "${userController.EnableUser.ApiOperation.Notes}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${authorizationHeader.description}", paramType = "header")
    })
    public void enableUser(@PathVariable("id") long id, @RequestParam("enabled") boolean enabled) {
        log.info(enabled ? "enable user id={}" : "disable user id={}", id);
        userService.enable(id, enabled);
    }

    @GetMapping("/email-verification")
    @ApiOperation(value = "The Verify User Email Web Service Endpoint", notes = "${userController.VerifyEmailUser.ApiOperation.Notes}")
    public ResponseEntity<MyHttpResponse> verifyEmailToken(@RequestParam("token") String token) {
        log.info("verify email with token={}", token);
        userService.verifyEmailToken(token);
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), "Your email was successfully verified.");
        return new ResponseEntity<>(myHttpResponse, HttpStatus.OK);
    }

    @GetMapping("/password-reset-request/{email}")
    @ApiOperation(value = "The Request Password Reset Web Service Endpoint", notes = "${userController.RequestPasswordReset.ApiOperation.Notes}")
    public ResponseEntity<MyHttpResponse> requestPasswordReset(@PathVariable("email") String email) {
        log.info("request password reset for email={}", email);
        userService.requestPasswordReset(email);
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), "The message with link to reset your password was sent to " + email);
        return new ResponseEntity<>(myHttpResponse, HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    @ApiOperation(value = "The Reset Password Web Service Endpoint", notes = "${userController.ResetPassword.ApiOperation.Notes}")
    public ResponseEntity<MyHttpResponse> resetPassword(@Valid @RequestBody PasswordResetModel passwordResetModel, BindingResult bindingResult) {
        ValidationUtil.validateTo(bindingResult);
        log.info("reset password for {}", passwordResetModel);
        userService.resetPassword(passwordResetModel.getToken(), passwordResetModel.getPassword());
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), "Your password was successfully reset");
        return new ResponseEntity<>(myHttpResponse, HttpStatus.OK);
    }

}
