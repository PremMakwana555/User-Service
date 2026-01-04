package com.prem.userservice.security.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "jwk_keys")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwkKey {
    @Id
    private String id;

    @Column(length = 4096)
    private String privateKey;

    @Column(length = 4096)
    private String publicKey;

    private Long createdAt;
}
