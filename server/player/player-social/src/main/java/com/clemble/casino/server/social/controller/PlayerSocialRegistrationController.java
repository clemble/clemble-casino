package com.clemble.casino.server.social.controller;

import static com.clemble.casino.web.player.PlayerWebMapping.*;
import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.registration.service.PlayerSocialRegistrationService;
import com.clemble.casino.server.event.player.SystemPlayerProfileRegistered;
import com.clemble.casino.server.social.ServerProfileSocialRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.registration.PlayerToken;
import com.clemble.casino.registration.PlayerSocialGrantRegistrationRequest;
import com.clemble.casino.registration.PlayerSocialRegistrationRequest;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.security.PlayerTokenFactory;
import com.clemble.casino.web.mapping.WebMapping;

@Controller
public class PlayerSocialRegistrationController implements PlayerSocialRegistrationService, ExternalController {

    final private PlayerTokenFactory playerTokenFactory;
    final private SystemNotificationService notificationService;
    final private ServerProfileSocialRegistrationService registrationService;
    final private ClembleCasinoValidationService validationService;

    public PlayerSocialRegistrationController(
        final PlayerTokenFactory playerTokenFactory,
        final ServerProfileSocialRegistrationService registrationService,
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
    public @ResponseBody PlayerToken createSocialPlayer(@RequestBody PlayerSocialRegistrationRequest socialRegistrationRequest) {
        validationService.validate(socialRegistrationRequest.getSocialConnectionData());
        // Step 1. Checking if this user already exists
        PlayerProfile playerProfile = registrationService.create(socialRegistrationRequest.getSocialConnectionData());
        // Step 2. Creating appropriate PlayerProfile
        PlayerToken token =  playerTokenFactory.create(playerProfile.getPlayer(), socialRegistrationRequest.getConsumerDetails());
        // Step 5. Notifying system of new user
        notificationService.notify(new SystemPlayerProfileRegistered(playerProfile.getPlayer(), playerProfile));
        // Step 6. All done continue
        return token;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = SOCIAL_REGISTRATION_GRANT, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody PlayerToken createSocialGrantPlayer(@RequestBody PlayerSocialGrantRegistrationRequest grantRegistrationRequest) {
        validationService.validate(grantRegistrationRequest.getAccessGrant());
        // Step 1. Checking if this user already exists
        PlayerProfile playerProfile = registrationService.create(grantRegistrationRequest.getAccessGrant());
        // Step 2. Creating appropriate PlayerProfile
        PlayerToken token =  playerTokenFactory.create(playerProfile.getPlayer(), grantRegistrationRequest.getConsumerDetails());
        // Step 5. Notifying system of new user
        notificationService.notify(new SystemPlayerProfileRegistered(playerProfile.getPlayer(), playerProfile));
        // Step 6. All done continue
        return token;
    }

}
