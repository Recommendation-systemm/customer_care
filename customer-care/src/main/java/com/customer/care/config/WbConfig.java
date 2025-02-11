//package com.customer.care.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class WbConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/login", "/auth","/layout","/home",  "/register", "/css/**", "/js/**").permitAll()
//                        .requestMatchers("/anonymous/**").permitAll()
//                        .requestMatchers("/dashboard", "/profile","/compliment/**","/complaint/**").authenticated() // Protected routes
//                        .requestMatchers("/admin/**").hasRole("ADMIN") // Routes for admin only
//                )
////                .formLogin(login -> login
////                        .loginPage("/auth/login")
////                        .defaultSuccessUrl("/redirect", true)
////                        .permitAll()
////                )
//                .formLogin(login -> login
////                        .loginPage("/login") // Custom login page
//                                .defaultSuccessUrl("/redirect", true)
////                        .defaultSuccessUrl("/",true) // Redirect after successful login
////                        .permitAll()
//                )
//                .logout(logout -> logout
////                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/")
////                        .permitAll()
//                );
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//}
