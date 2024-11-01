package com.scm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scm.repositories.UserRepo;

@Service
public class SecurityCustomUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepo UserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  UserRepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User does not exist"));
    }

}
