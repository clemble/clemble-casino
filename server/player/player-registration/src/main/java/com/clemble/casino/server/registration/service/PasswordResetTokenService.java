package com.clemble.casino.server.registration.service;

import com.clemble.casino.server.event.email.SystemEmailSendRequestEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.registration.ServerPasswordResetToken;
import com.clemble.casino.server.registration.repository.ServerPasswordResetTokenRepository;
import com.google.common.collect.ImmutableMap;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * Created by mavarazy on 2/2/15.
 */
public class PasswordResetTokenService {

    final private String ENCRYPT_SEPARATOR = ":";

    final private String host;
    final private TextEncryptor textEncryptor;
    final private PasswordResetTokenGenerator tokenGenerator;
    final private ServerPasswordResetTokenRepository tokenRepository;
    final private SystemNotificationService notificationService;

    public PasswordResetTokenService(
        String host,
        TextEncryptor textEncryptor,
        PasswordResetTokenGenerator tokenGenerator,
        ServerPasswordResetTokenRepository tokenRepository,
        SystemNotificationService notificationService) {
        this.host = host;
        this.textEncryptor = textEncryptor;
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
        String encryptedToken = textEncryptor.encrypt(token + ENCRYPT_SEPARATOR + player);
        String resetUrl = host + "#registration/reset/" + encryptedToken;
        notificationService.send(new SystemEmailSendRequestEvent(player, "restore_password", ImmutableMap.of("url", resetUrl)));
        // Step 4. Returning generated token
        return encryptedToken;
    }

    public String verify(String encryptedToken) {
        String decrypted = textEncryptor.decrypt(encryptedToken);
        int indexOfSeparator = decrypted.indexOf(ENCRYPT_SEPARATOR);
        if(indexOfSeparator == -1) {
            return null;
        }
        String token = decrypted.substring(0, indexOfSeparator);
        String player = decrypted.substring(indexOfSeparator + 1);
        // Step 1. Fetching player token
        ServerPasswordResetToken resetToken = tokenRepository.findOne(player);
        if (resetToken == null)
            return null;
        // Step 2. Removing reset token, so it could not be used again
        tokenRepository.delete(player);
        if (resetToken.getToken().equals(token))
            return player;
        return null;
    }

}
