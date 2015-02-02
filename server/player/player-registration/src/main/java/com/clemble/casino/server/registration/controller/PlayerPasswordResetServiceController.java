package com.clemble.casino.server.registration.controller;

import com.clemble.casino.registration.PlayerPasswordResetRequest;
import com.clemble.casino.registration.service.PlayerPasswordResetService;
import com.clemble.casino.server.registration.ServerPlayerCredential;
import com.clemble.casino.server.registration.repository.ServerPlayerCredentialRepository;
import com.clemble.casino.server.registration.service.PasswordResetTokenService;
import com.clemble.casino.server.registration.service.ServerPlayerCredentialManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mavarazy on 2/2/15.
 */
@RestController
public class PlayerPasswordResetServiceController implements PlayerPasswordResetService {

    final private PasswordResetTokenService tokenService;
    final private ServerPlayerCredentialManager credentialManager;

    public PlayerPasswordResetServiceController(
        PasswordResetTokenService tokenService,
        ServerPlayerCredentialManager credentialManager) {
        this.tokenService = tokenService;
        this.credentialManager = credentialManager;
    }

    @Override
    public boolean restore(String email) {
        // Step 1. Looking up player credentials
        String player = credentialManager.findPlayerByEmail(email);
        // Step 2. Checking player does exists
        if (player == null)
            return false;
        // Step 3. Generate and send email token
        tokenService.generateAndSend(player);
        // Step 4. Consider that everything done
        return true;
    }

    @Override
    public boolean reset(@Validated PlayerPasswordResetRequest request) {
        // Step 1. Checking player & token match
        boolean match = tokenService.match(request.getPlayer(), request.getToken());
        // Step 2. If they match update player password
        if (match) {
            credentialManager.update(request.getPlayer(), request.getPassword());
        }
        return match;
    }

}
