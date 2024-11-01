package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.service.SecurityCustomUserDetailsService;

@Configuration
public class SecurityConfig {

    // THIS IS FOR IN MEMORY LOGIN 
    // UserDetails user1 = User
    //                         .withDefaultPasswordEncoder()
    //                         .username("admin123")
    //                         .password("admin123")
    //                         .roles("ADMIN","USER")
    //                         .build();
    // UserDetails user2 = User.withDefaultPasswordEncoder()
    //                         .username("user123")
    //                         .password("password")
    //                         .build();
    // @Bean
    // public UserDetailsService userDetailsService(){
    //     return new InMemoryUserDetailsManager(user1,user2);
    // }
    // THIS IS FOR DATABASE LOGIN
    @Autowired
    private SecurityCustomUserDetailsService userDetailsService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    // THIS IS FOR AUTHENTICATING USER
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authenticationProvider(authenticationProvider());
        httpSecurity.authorizeHttpRequests((authorize) -> {
            // authorize.requestMatchers("/home","/register","/services").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        // Default login form
        // httpSecurity.formLogin(Customizer.withDefaults());
        // Customized login form
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            // formLogin.successForwardUrl("/user/dashboard");
            // formLogin.failureForwardUrl("/login?error=true");
            formLogin.defaultSuccessUrl("/user/profile", true); // Redirect to /user/dashboard after successful login
            formLogin.failureUrl("/login?error=true"); // Redirect to /login with error=true on failure
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
            formLogin.failureHandler(authFailureHandler);
        });

        // OAuth2 login
        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm->{
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
