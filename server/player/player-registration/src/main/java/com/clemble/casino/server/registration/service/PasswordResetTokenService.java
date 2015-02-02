package com.clemble.casino.server.registration.service;

import com.clemble.casino.server.event.email.SystemEmailSendRequestEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.registration.ServerPasswordResetToken;
import com.clemble.casino.server.registration.repository.ServerPasswordResetTokenRepository;
import com.google.common.collect.ImmutableMap;

import java.util.UUID;

/**
 * Created by mavarazy on 2/2/15.
 */
public class PasswordResetTokenService {

    final private String host;
    final private PasswordResetTokenGenerator tokenGenerator;
    final private ServerPasswordResetTokenRepository tokenRepository;
    final private SystemNotificationService notificationService;

    public PasswordResetTokenService(
        String host,
        PasswordResetTokenGenerator tokenGenerator,
        ServerPasswordResetTokenRepository tokenRepository,
        SystemNotificationService notificationService) {
        this.host = host;
        this.tokenGenerator = tokenGenerator;
        this.tokenRepository = tokenRepository;
        this.notificationService = notificationService;
    }

    public String generateAndSend(String player) {
        // Step 1. Generating random token
        String token = tokenGenerator.generate();
        // Step 2. Saving token for the player
        ServerPasswordResetToken serverToken = new ServerPasswordResetToken(player, token);
        tokenRepository.save(serverToken);
        // Step 3. Sending email notification to the user
        String resetUrl = host + "#/registration/reset/" + player + "/" + token;
        notificationService.send(new SystemEmailSendRequestEvent(player, "restore_email", ImmutableMap.of("url", resetUrl)));
        // Step 4. Returning generated token
        return token;
    }

    public boolean match(String player, String token) {
        // Step 1. Fetching player token
        ServerPasswordResetToken resetToken = tokenRepository.findOne(player);
        if (resetToken == null)
            return false;
        // Step 2. Removing reset token, so it could not be used again
        tokenRepository.delete(player);
        return resetToken.getToken().equals(token);
    }

}
