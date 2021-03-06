package com.chost.demo.security;

import com.chost.demo.model.entity.User;
import com.chost.demo.model.repository.UserRepository;
import com.chost.demo.model.wrappers.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        String log = s.replaceAll("@gmail.com","");
        User user = userRepository.findByLogin(s)
                .orElseThrow(()->new BadCredentialsException("No such user!"));

        return UserWrapper.create(user);
    }

    @Transactional
    public UserDetails loadUserById(int id){
        User user = userRepository.findById(id).orElseThrow(()->
                new UsernameNotFoundException("User not found with id "+id));
        return UserWrapper.create(user);
    }
}
