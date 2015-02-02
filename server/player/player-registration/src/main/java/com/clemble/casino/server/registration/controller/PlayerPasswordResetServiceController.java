package com.clemble.casino.server.registration.controller;

import com.clemble.casino.WebMapping;
import com.clemble.casino.registration.PlayerPasswordResetRequest;
import com.clemble.casino.registration.service.PlayerPasswordResetService;
import com.clemble.casino.server.registration.service.PasswordResetTokenService;
import com.clemble.casino.server.registration.service.ServerPlayerCredentialManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.clemble.casino.registration.RegistrationWebMapping.*;

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
    @RequestMapping(method = RequestMethod.GET, value = RESTORE_PASSWORD, produces = WebMapping.PRODUCES)
    public boolean restore(@RequestBody String email) {
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
    @RequestMapping(method = RequestMethod.POST, value = RESET_PASSWORD, produces = WebMapping.PRODUCES)
    public boolean reset(@Validated @RequestBody PlayerPasswordResetRequest request) {
        // Step 1. Checking player & token match
        String player = tokenService.verify(request.getToken());
        // Step 2. If they match update player password
        if (player != null) {
            // Case 1. Player was successfully fetched, and token was verified
            return credentialManager.update(player, request.getPassword()) != null;
        } else {
            // Case 2. Player or token illegal
            return false;
        }
    }

}
