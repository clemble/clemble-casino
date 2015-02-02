package com.clemble.casino.server.registration.service;

import com.clemble.casino.server.registration.ServerPlayerCredential;
import com.clemble.casino.server.registration.repository.ServerPlayerCredentialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by mavarazy on 2/2/15.
 */
public class ServerPlayerCredentialManager {

    final private PasswordEncoder passwordEncoder;
    final private ServerPlayerCredentialRepository credentialRepository;

    public ServerPlayerCredentialManager(
        PasswordEncoder passwordEncoder,
        ServerPlayerCredentialRepository credentialRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.credentialRepository = credentialRepository;
    }

    public ServerPlayerCredential save(String player, String email, String password) {
        // Step 1. Generating Server player credentials
        ServerPlayerCredential playerCredential = new ServerPlayerCredential(
            player,
            email,
            passwordEncoder.encode(password)
        );
        // Step 2. Saving Player credentials in repository
        return credentialRepository.save(playerCredential);
    }

    public ServerPlayerCredential update(String player, String password) {
        // Step 1. Looking up player credentials
        ServerPlayerCredential playerCredential = credentialRepository.findOne(player);
        // Step 2. Creating new player credentials
        ServerPlayerCredential newCredentials = new ServerPlayerCredential(
            playerCredential.getPlayer(),
            playerCredential.getEmail(),
            passwordEncoder.encode(password)
        );
        // Step 3. Saving new player credentials
        return credentialRepository.save(newCredentials);
    }

    public boolean matches(String email, String password) {
        // Step 1. Looking up player credentials
        ServerPlayerCredential playerCredential = credentialRepository.findByEmail(email);
        // Step 2. Checking password remainl
        return passwordEncoder.matches(password, playerCredential.getHash());
    }

    public String findPlayerByEmail(String email) {
        // Step 1. Fetching player credentials
        ServerPlayerCredential playerCredential = credentialRepository.findByEmail(email);
        // Step 2. Return player associated with the email
        return playerCredential != null ? playerCredential.getPlayer() : null;
    }
}
