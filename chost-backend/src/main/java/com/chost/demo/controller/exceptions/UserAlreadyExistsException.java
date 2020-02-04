package com.chost.demo.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Login already in use!")
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String msg){
        super(msg);
    }
}
