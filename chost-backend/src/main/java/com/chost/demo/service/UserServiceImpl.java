package com.chost.demo.service;

import com.chost.demo.controller.exceptions.UserAlreadyExistsException;
import com.chost.demo.model.dto.LoginRequest;
import com.chost.demo.model.dto.RegisterRequest;
import com.chost.demo.model.entity.Role;
import com.chost.demo.model.entity.User;
import com.chost.demo.model.repository.FileRepository;
import com.chost.demo.model.repository.UserRepository;
import com.chost.demo.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private FileRepository fileRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;
    private JwtTokenProvider tokenProvider;
    private AuthenticationManager authenticationManager;



    @Override
    public void registerUser(RegisterRequest registerRequest) throws UserAlreadyExistsException {
        if(userRepository.existsByLogin(registerRequest.getLogin())) {
            throw new UserAlreadyExistsException("Login address already in use.");
        }

        userRepository.save(mapToDao(registerRequest));

    }
    private User mapToDao(RegisterRequest registerRequest){
        User user = modelMapper.map(registerRequest, User.class);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(Collections.singletonList(Role.ROLE_USER));
        return user;
    }

    @Override
    public String loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(),
                        loginRequest.getPassword()
                )
        );
        return tokenProvider.createToken(authentication);
    }
}
