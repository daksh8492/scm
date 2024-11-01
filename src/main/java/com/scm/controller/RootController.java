package com.scm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.service.UserService;

@ControllerAdvice
public class RootController {

    @Autowired
    private UserService userService;
    
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @ModelAttribute
    public void addLoggedInUsesrInfo(Model model,Authentication authentication){
        if(authentication==null){
            return;
        }
        String username = Helper.getEmailOfLoggedInUsser(authentication);
        logger.info("Logging in user with email : "+username);
        User user = userService.getUserByEmail(username);
        System.out.println("Name : " + user.getName());
        System.out.println("Email : " + user.getEmail());
        model.addAttribute("LoggedInUser", user);
    }

}
