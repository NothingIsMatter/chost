package com.chost.demo.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Fail during file saving!")
public class FileStorageException extends RuntimeException {
    public FileStorageException(String s) {
        super(s);
    }
}
