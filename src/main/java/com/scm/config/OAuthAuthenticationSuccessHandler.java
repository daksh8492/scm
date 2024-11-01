package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        logger.info("OAuthAuthenticationSuccessHandler");


        var oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;

        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegistrationId);

        var oAuthuser = (DefaultOAuth2User)authentication.getPrincipal();

        oAuthuser.getAttributes().forEach((key,value)->{
            logger.info(key+" : "+value);
        });
        
        
        User user1 = new User();

        user1.setUserId(UUID.randomUUID().toString());
        user1.setRoleList(List.of(AppConstants.ROLE_USER));
        user1.setEmailVerified(true);
        user1.setEnabled(true);
        user1.setPassword("password");


        if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
            user1.setEmail(oAuthuser.getAttribute("email"));
            user1.setProfilePic(oAuthuser.getAttribute("picture"));
            user1.setName(oAuthuser.getAttribute("name"));
            user1.setProviderUserId(oAuthuser.getName());
            user1.setProvider(Providers.GOOGLE);
            user1.setAbout("This account is created using google");
        }
        else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){
            user1.setEmail(oAuthuser.getAttribute("email")!=null?oAuthuser.getAttribute("email"):oAuthuser.getAttribute("login")+"@gmail.com");
            user1.setProfilePic(oAuthuser.getAttribute("avatar_url"));
            user1.setName(oAuthuser.getAttribute("login"));
            user1.setProviderUserId(oAuthuser.getName());
            user1.setProvider(Providers.GITHUB);
            user1.setAbout("This account is created using github");
        }

        User user2 = userRepo.findByEmail(user1.getEmail()).orElse(null);
        if(user2==null){
            userRepo.save(user1);
            logger.info("User created with email " + user1.getEmail());
        }


      /*  DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        // logger.info(user.getName());
        // user.getAttributes().forEach((key,value)->{
        //     logger.info("{} => {}", key, null);
        // });
        // logger.info(user.getAuthorities().toString());

        // Log the user's name
        logger.info("User Name: {}", user.getName());

        // Log the user's attributes
        user.getAttributes().forEach((key, value) -> {
            logger.info("{} => {}", key, value);  // Correctly logging the key-value pairs
        });

        // Log the user's authorities
        logger.info("Authorities: {}", user.getAuthorities().toString());

        String email = user.getAttribute("email").toString();
        String name = user.getAttribute("name").toString();
        String picture = user.getAttribute("picture").toString();

        User user1 = new User();

        user1.setEmail(email);
        user1.setName(name);
        user1.setProfilePic(picture);
        user1.setPassword("password");
        user1.setUserId(UUID.randomUUID().toString());
        user1.setProvider(Providers.GOOGLE);
        user1.setEnabled(true);
        user1.setEmailVerified(true);
        user1.setProviderUserId(user.getName());
        user1.setRoleList(List.of("ROLE_USER"));
        user1.setAbout("This account is created using google");

        User user2 = userRepo.findByEmail(email).orElse(null);
        if(user2==null){
            userRepo.save(user1);
            logger.info("User created with email " + email);
        }*/


        new DefaultRedirectStrategy().sendRedirect(request, response,"/user/profile");
    }

}
