package com.chost.demo.model.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String login;
    private String name;
    private String password;
    private String email;
}
