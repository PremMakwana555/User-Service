package com.prem.userservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Token extends Base{
    @Column(nullable = false)
    private String tokenValue;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @ManyToOne
    private User user;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
