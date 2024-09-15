package com.prem.userservice.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User extends Base{
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false, unique = true)
    private String hashedPassword;
    @Enumerated(EnumType.STRING)
    private Role roles;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy = "token")
    private List<Token> tokens;
}

