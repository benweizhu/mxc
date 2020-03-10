package com.mxc.blog.springsecurityauthbasic.controller;

import com.mxc.blog.springsecurityauthbasic.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @GetMapping("/api/users/{id}")
  public ResponseEntity<User> getUser(@PathVariable long id) {
    return new ResponseEntity<>(new User(id), HttpStatus.OK);
  }
}
