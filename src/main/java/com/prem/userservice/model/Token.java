package com.prem.userservice.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Token extends Base{
    @Column(nullable = false)
    private String token;
    private boolean isUsed;
    @Enumerated(EnumType.STRING)
    private Status status;
    private boolean isExpired;
    private Date expiryDate;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
