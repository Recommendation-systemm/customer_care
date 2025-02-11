package com.customer.care.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandler() {
//        return (request, response, exception) -> {
//            if (exception instanceof DisabledException) {
//                response.sendRedirect("/auth/login?error=unverified");
//            } else {
//                response.sendRedirect("/auth/login?error=true");
//            }
//        };
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/","/feedback/**","/anonymous/**", "/register/**","/auth/**","/auth/verify", "/css/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .formLogin(login -> login
//                        .loginPage("/auth/login")
//                        .loginProcessingUrl("/login")
////                        .failureHandler(authenticationFailureHandler()) // Custom error handling
//                        .defaultSuccessUrl("/redirect", true)
//                        .permitAll()

                .formLogin(login -> login
                        .loginPage("/auth/login")  // Custom login page
                        .loginProcessingUrl("/login")  // Must match form action
                        .defaultSuccessUrl("/redirect", true)
                        .failureUrl("/auth/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
