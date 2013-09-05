package com.gogomaya.server.web.management;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.error.GogomayaValidationService;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.security.PlayerCredential;
import com.gogomaya.player.security.PlayerIdentity;
import com.gogomaya.player.service.PlayerRegistrationService;
import com.gogomaya.player.web.PlayerLoginRequest;
import com.gogomaya.player.web.PlayerRegistrationRequest;
import com.gogomaya.player.web.PlayerSocialRegistrationRequest;
import com.gogomaya.server.player.account.PlayerAccountServerService;
import com.gogomaya.server.player.registration.PlayerProfileRegistrationServerService;
import com.gogomaya.server.repository.player.PlayerCredentialRepository;
import com.gogomaya.server.repository.player.PlayerIdentityRepository;
import com.gogomaya.web.management.ManagementWebMapping;
import com.gogomaya.web.mapping.WebMapping;

@Controller
public class PlayerRegistrationController implements PlayerRegistrationService {

    final private PlayerCredentialRepository playerCredentialRepository;
    final private PlayerIdentityRepository playerIdentityRepository;
    final private PlayerProfileRegistrationServerService playerProfileRegistrationService;
    final private GogomayaValidationService validationService;
    final private PlayerAccountServerService playerAccountServerService;

    public PlayerRegistrationController(final PlayerProfileRegistrationServerService playerProfileRegistrationService,
            final PlayerCredentialRepository playerCredentialRepository,
            final PlayerIdentityRepository playerIdentityRepository,
            final GogomayaValidationService validationService,
            final PlayerAccountServerService playerAccountServerService) {
        this.playerAccountServerService = checkNotNull(playerAccountServerService);
        this.playerProfileRegistrationService = checkNotNull(playerProfileRegistrationService);
        this.playerCredentialRepository = checkNotNull(playerCredentialRepository);
        this.playerIdentityRepository = checkNotNull(playerIdentityRepository);
        this.validationService = checkNotNull(validationService);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = ManagementWebMapping.PLAYER_REGISTRATION_LOGIN, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PlayerIdentity login(@RequestBody PlayerLoginRequest loginRequest) {
        PlayerCredential playerCredentials = loginRequest.getPlayerCredential();
        // Step 1. Fetch saved player credentials
        PlayerCredential fetchedCredentials = playerCredentialRepository.findByEmail(playerCredentials.getEmail());
        // Step 2. If there is no such credentials, than user is unregistered
        if (fetchedCredentials == null)
            throw GogomayaException.fromError(GogomayaError.EmailOrPasswordIncorrect);
        // Step 3. Compare passwords
        if (!fetchedCredentials.getPassword().equals(playerCredentials.getPassword()))
            throw GogomayaException.fromError(GogomayaError.EmailOrPasswordIncorrect);
        // Step 4. Everything is fine, return Identity
        PlayerIdentity playerIdentity = loginRequest.getPlayerIdentity();
        playerIdentity.setPlayerId(fetchedCredentials.getPlayerId());
        return playerIdentityRepository.saveAndFlush(playerIdentity);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = ManagementWebMapping.PLAYER_REGISTRATION_SIGN_IN, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerIdentity createPlayer(@RequestBody final PlayerRegistrationRequest registrationRequest) {
        // Step 1. Validating input data prior to any actions
        PlayerIdentity playerIdentity = restoreUser(registrationRequest);
        if (playerIdentity != null)
            return playerIdentity;
        validationService.validate(registrationRequest.getPlayerProfile());
        // Step 2. Creating appropriate PlayerProfile
        PlayerProfile playerProfile = playerProfileRegistrationService.createPlayerProfile(registrationRequest.getPlayerProfile());
        // Step 3. Registration done through separate registration service
        return register(registrationRequest, playerProfile);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = ManagementWebMapping.PLAYER_REGISTRATION_SOCIAL, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerIdentity createSocialPlayer(@RequestBody PlayerSocialRegistrationRequest socialRegistrationRequest) {
        // Step 1. Checking if this user already exists
        PlayerIdentity playerIdentity = restoreUser(socialRegistrationRequest);
        if (playerIdentity != null)
            return playerIdentity;
        // Step 2. Creating appropriate PlayerProfile
        validationService.validate(socialRegistrationRequest.getSocialConnectionData());
        // Step 3. Registering player with SocialConnection
        PlayerProfile playerProfile = playerProfileRegistrationService.createPlayerProfile(socialRegistrationRequest.getSocialConnectionData());
        // Step 4. Register new user and identity
        return register(socialRegistrationRequest, playerProfile);
    }

    private PlayerIdentity restoreUser(PlayerLoginRequest loginRequest) {
        // Step 1. Checking request is valid
        validationService.validate(loginRequest);
        validationService.validate(loginRequest.getPlayerCredential());
        validationService.validate(loginRequest.getPlayerIdentity());
        // Step 2. Checking this is a new user or an old one
        PlayerCredential playerCredentials = loginRequest.getPlayerCredential();
        // Step 3. Fetch associated player credentials
        PlayerCredential fetchedCredentials = playerCredentialRepository.findByEmail(playerCredentials.getEmail());
        // Step 2. If there is such credentials, than user already registered
        if (fetchedCredentials != null) {
            // Step 2.1 If the password is the same, just return identity to the user
            if (playerCredentials.getPassword().equals(fetchedCredentials.getPassword())) {
                return playerIdentityRepository.findOne(fetchedCredentials.getPlayerId());
            } else {
                // Step 2.2 If password does not match this is an error
                throw GogomayaException.fromError(GogomayaError.EmailAlreadyRegistered);
            }
        }
        return null;
    }

    public PlayerIdentity register(final PlayerLoginRequest loginRequest, final PlayerProfile playerProfile) {
        // Step 0. Registering first wallet
        playerAccountServerService.register(playerProfile);
        // Step 1. Create new credentials
        PlayerCredential playerCredentials = loginRequest.getPlayerCredential().setPlayerId(playerProfile.getPlayerId());
        playerCredentials = playerCredentialRepository.saveAndFlush(playerCredentials);
        // Step 2. Create new identity
        PlayerIdentity playerIdentity = loginRequest.getPlayerIdentity().setPlayerId(playerProfile.getPlayerId());
        playerIdentity = playerIdentityRepository.saveAndFlush(playerIdentity);
        // Step 3. Sending player identity
        return playerIdentity;
    }

}
