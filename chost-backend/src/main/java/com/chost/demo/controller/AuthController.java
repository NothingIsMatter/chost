package com.chost.demo.controller;

import com.chost.demo.controller.exceptions.UserAlreadyExistsException;
import com.chost.demo.model.dto.LoginRequest;
import com.chost.demo.model.dto.RegisterRequest;
import com.chost.demo.model.entity.Role;
import com.chost.demo.model.entity.User;
import com.chost.demo.model.repository.UserRepository;
import com.chost.demo.security.jwt.JwtTokenProvider;
import com.chost.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;


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
