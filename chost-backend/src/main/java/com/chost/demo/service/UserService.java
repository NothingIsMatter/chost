package com.chost.demo.service;


import com.chost.demo.controller.exceptions.UserAlreadyExistsException;
import com.chost.demo.model.dto.LoginRequest;
import com.chost.demo.model.dto.RegisterRequest;


public interface UserService {
    void registerUser(RegisterRequest registerRequest) throws UserAlreadyExistsException;
    String loginUser(LoginRequest loginRequest);

}
