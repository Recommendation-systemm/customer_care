package com.customer.care.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.customer.care.entities.AppUser;
import com.customer.care.entities.Role;
import com.customer.care.repositories.UserRepository;
import com.customer.care.repositories.RoleRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public void registerUser(AppUser user) {
//        String hashedPassword = hashPassword(user.getPassword());
//        user.setPassword(hashedPassword);
//
//        Role defaultRole = roleRepository.findByName("ROLE_CUSTOMER");
//        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);
    }

    public AppUser loginUser(String email, String password) {
        AppUser user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(hashPassword(password))) {
            return user;
        } else {
            return null;
        }
    }


    private String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(password.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser= userRepository.findByEmail(username);
//        var bCryptEncoder= new BCryptPasswordDecoder();
        if(appUser!=null){
            return User.withUsername(appUser.getEmail())
                    .password(appUser.getPassword())
                    .roles(appUser.getPrivilege())
                    .build();
        }
        return null;
    }

    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
