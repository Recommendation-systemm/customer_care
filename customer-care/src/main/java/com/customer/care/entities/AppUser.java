package com.customer.care.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String username;
    private String password;
    private String email;
    private String privilege;
    private String phone;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles;



}
