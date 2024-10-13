package com.prem.userservice.repository;

import com.prem.userservice.model.Status;
import com.prem.userservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    Optional<Token> findByTokenValue(String tokenValue);
}
