package com.clemble.casino.server.email.service;

import com.clemble.casino.player.PlayerEmailWebMapping;
import com.clemble.casino.player.service.PlayerEmailService;
import com.clemble.casino.server.email.PlayerEmail;
import com.clemble.casino.server.email.repository.PlayerEmailRepository;
import com.clemble.casino.server.event.email.SystemEmailVerifiedEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * Created by mavarazy on 12/6/14.
 */
public class ServerPlayerEmailService implements PlayerEmailService {

    final private Logger LOG = LoggerFactory.getLogger(ServerPlayerEmailService.class);

    final private static String ENCRYPT_SEPARATOR = ":";

    final private String host;
    final private TextEncryptor textEncryptor;
    final private ServerEmailSender emailService;
    final private PlayerEmailRepository emailRepository;
    final private SystemNotificationService systemNotificationService;

    public ServerPlayerEmailService(String host, TextEncryptor textEncryptor, ServerEmailSender emailService, PlayerEmailRepository emailRepository, SystemNotificationService systemNotificationService) {
        this.host = PlayerEmailWebMapping.toEmailUrl("/").replace("{host}", "api" + host);
        this.textEncryptor = textEncryptor;
        this.emailService = emailService;
        this.emailRepository = emailRepository;
        this.systemNotificationService = systemNotificationService;
    }

    public String add(String player, String email, boolean verified) {
        if(player == null || email == null) {
            LOG.debug("{} player or email is empty", player);
            return null;
        }
        LOG.debug("{} adding email", player);
        // Step 1. Verifying there is no email associated for the player
        PlayerEmail playerEmail = emailRepository.findOne(player);
        if (playerEmail != null) {
            LOG.debug("{} player email already defined", player);
            return null;
        }
        // Step 2. No need to verify email
        if (verified) {
            LOG.debug("{} player email was verified", player);
            add(new PlayerEmail(player, email));
            return null;
        }
        // Step 3. Generating url
        // WARNING do not change the order, this order is in order to keep no limit on player ids
        String url = host + "verify?code=" + textEncryptor.encrypt(email + ENCRYPT_SEPARATOR + player);
        // Step 4. Sending verification email
        emailService.sendVerification(email, url);
        // Step 5. Returning url
        return url;
    }

    @Override
    public boolean verify(String verificationCode) {
        // Step 1. Decrypting email
        String decrypted = textEncryptor.decrypt(verificationCode);
        int indexOfSeparator = decrypted.indexOf(ENCRYPT_SEPARATOR);
        if(indexOfSeparator == -1) {
            LOG.error("player email is verified {}", verificationCode);
            return false;
        }
        String email = decrypted.substring(0, indexOfSeparator);
        String player = decrypted.substring(indexOfSeparator + 1);
        // Step 2. Checking value
        LOG.debug("{} player email was verified", player);
        add(new PlayerEmail(player, email));
        return true;
    }

    private void add(PlayerEmail playerEmail) {
        LOG.debug("{} adding email", playerEmail.getPlayer());
        // Step 1. Saving player email in repository
        emailRepository.save(playerEmail);
        // Step 2. Sending notification
        systemNotificationService.send(new SystemEmailVerifiedEvent(playerEmail.getPlayer()));
    }
}
