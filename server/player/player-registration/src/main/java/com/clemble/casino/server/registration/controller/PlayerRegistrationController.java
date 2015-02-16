package com.clemble.casino.server.registration.controller;

import static com.clemble.casino.registration.RegistrationWebMapping.*;
import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.registration.PlayerLoginRequest;
import com.clemble.casino.registration.service.PlayerRegistrationService;
import com.clemble.casino.server.event.email.SystemEmailAddedEvent;
import com.clemble.casino.server.event.player.SystemPlayerImageChangedEvent;
import com.clemble.casino.server.event.player.SystemPlayerProfileRegisteredEvent;
import com.clemble.casino.server.registration.PlayerKeyGenerator;
import com.clemble.casino.server.registration.service.GravatarService;
import com.clemble.casino.server.registration.service.ServerPlayerCredentialManager;
import com.clemble.casino.server.security.PlayerTokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.registration.PlayerRegistrationRequest;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.WebMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class PlayerRegistrationController implements PlayerRegistrationService, ExternalController {
    // !!!TODO need a safe restoration process for all Registrations not only for login!!!

    final private PlayerTokenUtils tokenUtils;
    final private PlayerKeyGenerator playerKeyGenerator;
    final private ServerPlayerCredentialManager credentialManager;
    final private SystemNotificationService notificationService;

    public PlayerRegistrationController(
        ServerPlayerCredentialManager credentialManager,
        PlayerTokenUtils tokenUtils,
        PlayerKeyGenerator playerKeyGenerator,
        SystemNotificationService notificationService) {
        this.tokenUtils = checkNotNull(tokenUtils);
        this.playerKeyGenerator = checkNotNull(playerKeyGenerator);
        this.credentialManager = credentialManager;
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public PlayerLoginRequest login(PlayerLoginRequest loginRequest) {
        String player = credentialManager.verify(loginRequest.getEmailOrNickName(), loginRequest.getPassword());
        // Step 1. Checking password match
        if (player == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.EmailOrPasswordIncorrect);
        // Step 4. Everything is fine, return Identity
        return new PlayerLoginRequest(player, loginRequest.getEmailOrNickName(), loginRequest.getPassword());
    }

    @RequestMapping(method = RequestMethod.POST, value = REGISTRATION_LOGIN, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public PlayerLoginRequest httpLogin(@Valid @RequestBody PlayerLoginRequest playerCredentials, HttpServletResponse response) {
        PlayerLoginRequest loginResponse = login(playerCredentials);
        tokenUtils.updateResponse(loginResponse.getPlayer(), response);
        return loginResponse;
    }

    @Override
    public PlayerRegistrationRequest register(final PlayerRegistrationRequest registrationRequest) {
        // Step 1.1 Checking user not already exists
        String registeredPlayer = credentialManager.findPlayerByEmail(registrationRequest.getEmail());
        if (registeredPlayer != null) {
            PlayerLoginRequest loginRequest = new PlayerLoginRequest(registrationRequest.getEmail(), registrationRequest.getPassword());
            // Step 1.2. Verify password matches
            String player = login(loginRequest).getPlayer();
            // Step 1.3. Returning validated profile
            return registrationRequest.copyWithPlayer(player);
        }
        // Step 2. Creating appropriate PlayerProfile
        String player = playerKeyGenerator.generate();
        // Step 3. Adding initial fields to PlayerProfile
        PlayerProfile normalizedProfile = registrationRequest.toProfileWithPlayer(player);
        if (credentialManager.existsByNickname(normalizedProfile.getNickName()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.NickOccupied);
        // Step 4. Registration done through separate registration service
        register(player, registrationRequest.getEmail(), normalizedProfile.getNickName(), registrationRequest.getPassword());
        // Step 5. Notifying system of new user
        notificationService.send(new SystemPlayerProfileRegisteredEvent(player, normalizedProfile));
        // Step 5.1. Creating email added event
        notificationService.send(new SystemEmailAddedEvent(player, registrationRequest.getEmail(), false));
        // Step 6. All done returning response
        return registrationRequest.copyWithPlayer(player);
    }

    @RequestMapping(method = RequestMethod.POST, value = REGISTRATION_PROFILE, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public PlayerRegistrationRequest httpRegister(@Valid @RequestBody final PlayerRegistrationRequest registrationRequest, HttpServletResponse response) {
        PlayerRegistrationRequest player = register(registrationRequest);
        tokenUtils.updateResponse(player.getPlayer(), response);
        return player;
    }

    public String register(final String player, String email, final String nickName, String password) {
        // Step 1. Create new credentials
        credentialManager.save(player, email, nickName, password);
        // Step 2. Generating default image redirect
        String imageRedirect = GravatarService.toRedirect(email);
        notificationService.send(new SystemPlayerImageChangedEvent(player, imageRedirect, imageRedirect + "?s=48"));
        // Step 2.1 Specifying player type
        // TODO move to registration listener playerProfile.setType(PlayerType.free);
        // Step 4. Returning generated player token
        return player;
    }

}
