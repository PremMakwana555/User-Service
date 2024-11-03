package com.prem.userservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends Base {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false, unique = true)
    private String hashedPassword;

    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_roles", // Join table name
//            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), // FK in join table for User
//            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") // FK in join table for Role
//    )
    private List<UserRole> roles;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Token> tokens = new ArrayList<>();
}

