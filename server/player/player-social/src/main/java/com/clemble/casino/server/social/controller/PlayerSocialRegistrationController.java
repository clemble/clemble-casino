package com.clemble.casino.server.social.controller;

import static com.clemble.casino.social.SocialWebMapping.*;
import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.registration.service.PlayerSocialRegistrationService;
import com.clemble.casino.server.social.SocialConnectionDataAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.registration.PlayerToken;
import com.clemble.casino.registration.PlayerSocialGrantRegistrationRequest;
import com.clemble.casino.registration.PlayerSocialRegistrationRequest;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.security.PlayerTokenFactory;
import com.clemble.casino.WebMapping;

@RestController
public class PlayerSocialRegistrationController implements PlayerSocialRegistrationService, ExternalController {

    final private PlayerTokenFactory playerTokenFactory;
    final private SystemNotificationService notificationService;
    final private SocialConnectionDataAdapter registrationService;
    final private ClembleCasinoValidationService validationService;

    public PlayerSocialRegistrationController(
        final PlayerTokenFactory playerTokenFactory,
        final SocialConnectionDataAdapter registrationService,
        final ClembleCasinoValidationService validationService,
        final SystemNotificationService notificationService) {
        this.playerTokenFactory = checkNotNull(playerTokenFactory);
        this.registrationService = registrationService;
        this.validationService = checkNotNull(validationService);
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = SOCIAL_REGISTRATION_DESCRIPTION, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public PlayerToken createSocialPlayer(@RequestBody PlayerSocialRegistrationRequest socialRegistrationRequest) {
        validationService.validate(socialRegistrationRequest.getSocialConnectionData());
        // Step 1. Checking if this user already exists
        String player = registrationService.register(socialRegistrationRequest.getSocialConnectionData());
        // Step 2. Creating appropriate PlayerProfile
        PlayerToken token =  playerTokenFactory.create(player, socialRegistrationRequest.getConsumerDetails());
        // Step 3. All done continue
        return token;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = SOCIAL_REGISTRATION_GRANT, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public PlayerToken createSocialGrantPlayer(@RequestBody PlayerSocialGrantRegistrationRequest grantRegistrationRequest) {
        validationService.validate(grantRegistrationRequest.getAccessGrant());
        // Step 1. Checking if this user already exists
        String player = registrationService.register(grantRegistrationRequest.getAccessGrant());
        // Step 2. Creating appropriate PlayerProfile
        PlayerToken token =  playerTokenFactory.create(player, grantRegistrationRequest.getConsumerDetails());
        // Step 3. All done continue
        return token;
    }

}
