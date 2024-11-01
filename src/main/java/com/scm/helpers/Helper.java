package com.scm.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUsser(Authentication authentication){

        if(authentication instanceof OAuth2AuthenticationToken){
            var oAuth2AuthenticationToken =(OAuth2AuthenticationToken)authentication;
            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            System.out.println(clientId);

            var oAuthuser = (DefaultOAuth2User)authentication.getPrincipal();

            String username;

            if(clientId.equalsIgnoreCase("google")){
                System.out.println("Getting email from google");
                username = oAuthuser.getAttribute("email");
            }
            else{
                System.out.println("Getting email from github");
                username = oAuthuser.getAttribute("email")!=null?oAuthuser.getAttribute("email"):oAuthuser.getAttribute("login")+"@gmail.com";
            }

            return username;
        }
        else{
            System.out.println("Getting email from database");
            return authentication.getName();
        }

    }

    public static String getLinkForEmailVerification(String emailToken){
        String link = "http://localhost:8080/auth/verify-email?token="+emailToken;
        return link;
    }

}
