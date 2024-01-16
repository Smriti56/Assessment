package com.example.assessment.controller;

import com.example.assessment.domain.UserInformation;
import com.example.assessment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @PostMapping("/add")
    public ResponseEntity<UserInformation> createPayment(@RequestBody UserInformation userInformation){

        UserInformation userInformationResponse = userService.createUser(userInformation);
        return new ResponseEntity<>(userInformationResponse, HttpStatus.CREATED);
    }
}
