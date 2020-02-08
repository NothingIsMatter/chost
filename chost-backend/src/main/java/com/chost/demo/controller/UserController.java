package com.chost.demo.controller;

import com.chost.demo.model.dto.jsonview.View;
import com.chost.demo.model.entity.User;
import com.chost.demo.model.repository.UserRepository;
import com.chost.demo.model.wrappers.UserWrapper;
import com.fasterxml.jackson.annotation.JsonView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping("/me")
    @CrossOrigin(origins = "http://localhost:8080")
    @Secured("ROLE_USER")
    @JsonView(View.ME.class)
    public User me(@AuthenticationPrincipal UserWrapper user){
        return userRepository.findByLogin(user.getLogin()).get();
    }
    @GetMapping("/about")
    @CrossOrigin(origins = "http://localhost:8080")
    @Secured("ROLE_USER")
    @JsonView(View.FULLINFORMATION.class)
    public User about(@AuthenticationPrincipal UserWrapper user){
        return userRepository.findByLogin(user.getLogin()).get();
    }

}
