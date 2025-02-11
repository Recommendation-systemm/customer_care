package com.customer.care.services;

import com.customer.care.entities.Role;
import com.customer.care.entities.RoleName;
import com.customer.care.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.customer.care.entities.AppUser;
import com.customer.care.repositories.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}") private String sender;
    @Value("${email.verification.url}") private String emailVerificationUrl;


    public boolean verifyEmailToken(String token) {
        Optional<AppUser> userOptional = userRepository.findByVerificationToken(token);

        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            user.setVerified(true);
            user.setVerificationToken(null); // Remove token after verification
            userRepository.save(user);
            return true;
        }
        return false;
    }


//    public AppUser authenticate(String email, String password) {
//        AppUser user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new RuntimeException("Invalid email or password.");
//        }
//
////        if (!user.isVerified()) {
////            throw new RuntimeException("Please verify your email before logging in.");
////        }
//
//        return user; // Authentication successful
//    }

    public void registerUser(AppUser user) {
//        String hashedPassword = hashPassword(user.getPassword());
//        user.setPassword(hashedPassword);
//
//        Role defaultRole = roleRepository.findByName("ROLE_CUSTOMER");
//        user.setRoles(Set.of(defaultRole));
        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.addRole(userRole);
        user.setVerified(false); // Default false
        user.setVerificationToken(UUID.randomUUID().toString());

        userRepository.save(user);
        sendVerificationEmail(user);
    }


    private void sendVerificationEmail(AppUser user) {
        try {


            System.out.println("Start of sending email.....................");
            String subject = "Verify Your Email";
            String url = emailVerificationUrl+"/auth/verify?token=" + user.getVerificationToken();
            String message = "Click the link to verify your email: " + url;

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            System.out.println("Sending email...........................");
            mailSender.send(mailMessage);
            System.out.println("Mail Sent!");
        }catch (Exception e){
            System.out.println("Email Failed!! "+e.getMessage());
        }
    }

    public void registerAdmin(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign "ADMIN" role
        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.addRole(adminRole);

        userRepository.save(user);
    }

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public void registerUser(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole(Role.USER); // Default role for new users
//        userRepository.save(user);
//    }
//
//    public void registerAdmin(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole(Role.ADMIN); // Admin role
//        userRepository.save(user);
//    }

    public AppUser loginUser(String email, String password) {
        AppUser user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found"));

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
        AppUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isVerified()) {
            throw new DisabledException("Please verify your email before logging in.");
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())) // Ensure "ROLE_" prefix
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), authorities);
    }

    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

}
