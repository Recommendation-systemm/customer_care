package com.customer.care.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WbConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll() // Unprotected routes
                        .requestMatchers("/dashboard", "/profile").authenticated() // Protected routes
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Routes for admin only
                )
                .formLogin(login -> login
//                        .loginPage("/login") // Custom login page
                        .defaultSuccessUrl("/",true) // Redirect after successful login
//                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.build();
    }
}
