package com.igar15.training_management.controller;

import com.igar15.training_management.entity.User;
import com.igar15.training_management.service.UserService;
import com.igar15.training_management.to.MyHttpResponse;
import com.igar15.training_management.to.UserTo;
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

    @Autowired
    private UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<User>> getUsers(@SortDefault("email") Pageable pageable) {
        Page<User> users = userService.getUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserTo userTo) {
        User user = userService.createUser(userTo);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody UserTo userTo) {
        User user = userService.updateUser(id, userTo);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MyHttpResponse> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        MyHttpResponse myHttpResponse = new MyHttpResponse(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.getReasonPhrase(), "User deleted successfully");
        return new ResponseEntity<>(myHttpResponse, HttpStatus.NO_CONTENT);
    }






}
