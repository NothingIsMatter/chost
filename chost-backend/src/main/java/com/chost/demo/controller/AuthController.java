package com.chost.demo.controller;

import com.chost.demo.controller.exceptions.UserAlreadyExistsException;
import com.chost.demo.model.dto.LoginRequest;
import com.chost.demo.model.dto.RegisterRequest;
import com.chost.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

       String token = userService.loginUser(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup")

    public ResponseEntity<?> registerUser( @RequestBody RegisterRequest signUpRequest) throws UserAlreadyExistsException {
        userService.registerUser(signUpRequest);
        return ResponseEntity.ok().body( "User registered successfully");
    }
}
