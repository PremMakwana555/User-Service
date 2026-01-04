package com.prem.userservice.security.service;

import com.nimbusds.jose.jwk.RSAKey;
import com.prem.userservice.security.models.JwkKey;
import com.prem.userservice.security.repository.JwkKeyRepository;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@Service
public class KeyManager {

    private final JwkKeyRepository jwkKeyRepository;

    public KeyManager(JwkKeyRepository jwkKeyRepository) {
        this.jwkKeyRepository = jwkKeyRepository;
    }

    public RSAKey getRSAKey() {
        return jwkKeyRepository.findAll().stream().findFirst()
                .map(this::loadKey)
                .orElseGet(this::generateAndSaveKey);
    }

    private RSAKey loadKey(JwkKey jwkKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            byte[] publicBytes = Base64.getDecoder().decode(jwkKey.getPublicKey());
            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicBytes);
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicSpec);

            byte[] privateBytes = Base64.getDecoder().decode(jwkKey.getPrivateKey());
            PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateBytes);
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateSpec);

            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(jwkKey.getId())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load RSA key from database", e);
        }
    }

    private RSAKey generateAndSaveKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            String id = UUID.randomUUID().toString();
            JwkKey jwkKey = new JwkKey(
                    id,
                    Base64.getEncoder().encodeToString(privateKey.getEncoded()),
                    Base64.getEncoder().encodeToString(publicKey.getEncoded()),
                    System.currentTimeMillis());

            jwkKeyRepository.save(jwkKey);

            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(id)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate RSA key", e);
        }
    }
}
