package com.prem.userservice.security.repository;

import com.prem.userservice.security.models.JwkKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwkKeyRepository extends JpaRepository<JwkKey, String> {
}
