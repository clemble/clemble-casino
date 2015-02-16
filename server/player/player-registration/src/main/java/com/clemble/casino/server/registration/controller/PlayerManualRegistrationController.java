package com.clemble.casino.server.registration.controller;

import static com.clemble.casino.registration.RegistrationWebMapping.*;
import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.registration.service.PlayerManualRegistrationService;
import com.clemble.casino.server.event.email.SystemEmailAddedEvent;
import com.clemble.casino.server.event.player.SystemPlayerImageChangedEvent;
import com.clemble.casino.server.event.player.SystemPlayerProfileRegisteredEvent;
import com.clemble.casino.server.registration.PlayerKeyGenerator;
import com.clemble.casino.server.registration.service.GravatarService;
import com.clemble.casino.server.registration.service.ServerPlayerCredentialManager;
import com.clemble.casino.server.security.PlayerTokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.registration.PlayerCredential;
import com.clemble.casino.registration.PlayerLoginRequest;
import com.clemble.casino.registration.PlayerRegistrationRequest;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.WebMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class PlayerManualRegistrationController implements PlayerManualRegistrationService, ExternalController {
    // !!!TODO need a safe restoration process for all Registrations not only for login!!!

    final private PlayerTokenUtils tokenUtils;
    final private PlayerKeyGenerator playerKeyGenerator;
    final private ServerPlayerCredentialManager credentialManager;
    final private SystemNotificationService notificationService;
    final private ClembleCasinoValidationService validationService;

    public PlayerManualRegistrationController(
        ServerPlayerCredentialManager credentialManager,
        PlayerTokenUtils tokenUtils,
        PlayerKeyGenerator playerKeyGenerator,
        ClembleCasinoValidationService validationService,
        SystemNotificationService notificationService) {
        this.tokenUtils = checkNotNull(tokenUtils);
        this.playerKeyGenerator = checkNotNull(playerKeyGenerator);
        this.credentialManager = credentialManager;
        this.validationService = checkNotNull(validationService);
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public String login(PlayerCredential playerCredentials) {
        // Step 1. Checking password match
        if (!credentialManager.matches(playerCredentials.getEmail(), playerCredentials.getPassword()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.EmailOrPasswordIncorrect);
        // Step 4. Everything is fine, return Identity
        return credentialManager.findPlayerByEmail(playerCredentials.getEmail());
    }

    @RequestMapping(method = RequestMethod.POST, value = REGISTRATION_LOGIN, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public String httpLogin(@Validated @RequestBody PlayerCredential playerCredentials, HttpServletResponse response) {
        String player = login(playerCredentials);
        tokenUtils.updateResponse(player, response);
        return player;
    }

    @Override
    public String createPlayer(final PlayerRegistrationRequest registrationRequest) {
        // Step 1. Validating input data prior to any actions
        validationService.validate(registrationRequest.getPlayerCredential());
        validationService.validate(registrationRequest.getPlayerProfile());
        // Step 1.1 Checking user not already exists
        if (null != credentialManager.findPlayerByEmail(registrationRequest.getPlayerCredential().getEmail()))
            return login(registrationRequest.getPlayerCredential());
        // Step 2. Creating appropriate PlayerProfile
        String player = playerKeyGenerator.generate();
        // Step 3. Adding initial fields to PlayerProfile
        if (registrationRequest.getPlayerProfile() == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.ProfileInvalid);
        if (registrationRequest.getPlayerProfile().getSocialConnections() != null && !registrationRequest.getPlayerProfile().getSocialConnections().isEmpty())
            throw ClembleCasinoException.fromError(ClembleCasinoError.ProfileSocialMustBeEmpty);
        PlayerProfile normalizedProfile = registrationRequest.getPlayerProfile();
        normalizedProfile.setPlayer(player);
        if(normalizedProfile.getNickName() == null) {
            String email = registrationRequest.getPlayerCredential().getEmail();
            normalizedProfile.setNickName(email.substring(0, email.indexOf("@")));
        }
        validationService.validate(normalizedProfile);
        // Step 4. Registration done through separate registration service
        register(registrationRequest.getPlayerCredential(), player);
        // Step 5. Notifying system of new user
        notificationService.send(new SystemPlayerProfileRegisteredEvent(player, normalizedProfile));
        // Step 5.1. Creating email added event
        notificationService.send(new SystemEmailAddedEvent(player, registrationRequest.getPlayerCredential().getEmail(), false));
        // Step 6. All done returning response
        return player;
    }

    @RequestMapping(method = RequestMethod.POST, value = REGISTRATION_PROFILE, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public String httpCreatePlayer(@Validated @RequestBody final PlayerRegistrationRequest registrationRequest, HttpServletResponse response) {
        String player = createPlayer(registrationRequest);
        tokenUtils.updateResponse(player, response);
        return player;
    }

    public String register(final PlayerCredential playerCredentials, final String player) {
        // Step 1. Create new credentials
        credentialManager.save(player, playerCredentials.getEmail(), playerCredentials.getPassword());
        // Step 2. Generating default image redirect
        String imageRedirect = GravatarService.toRedirect(playerCredentials.getEmail());
        notificationService.send(new SystemPlayerImageChangedEvent(player, imageRedirect, imageRedirect + "?s=48"));
        // Step 2.1 Specifying player type
        // TODO move to registration listener playerProfile.setType(PlayerType.free);
        // Step 4. Returning generated player token
        return player;
    }

}
