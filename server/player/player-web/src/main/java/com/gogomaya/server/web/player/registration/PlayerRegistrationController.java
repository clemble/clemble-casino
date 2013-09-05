package com.gogomaya.server.web.player.registration;

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
import com.gogomaya.player.SocialConnectionData;
import com.gogomaya.player.security.PlayerCredential;
import com.gogomaya.player.security.PlayerIdentity;
import com.gogomaya.player.service.PlayerRegistrationService;
import com.gogomaya.player.web.RegistrationRequest;
import com.gogomaya.server.player.registration.PlayerRegistrationServerService;
import com.gogomaya.server.repository.player.PlayerCredentialRepository;
import com.gogomaya.server.repository.player.PlayerIdentityRepository;
import com.gogomaya.server.social.SocialConnectionDataAdapter;
import com.gogomaya.web.mapping.WebMapping;
import com.gogomaya.web.player.PlayerWebMapping;

@Controller
public class PlayerRegistrationController implements PlayerRegistrationService {

    final private PlayerRegistrationServerService playerRegistrationService;
    final private PlayerCredentialRepository playerCredentialRepository;
    final private PlayerIdentityRepository playerIdentityRepository;
    final private SocialConnectionDataAdapter socialConnectionDataAdapter;
    final private GogomayaValidationService validationService;

    public PlayerRegistrationController(final PlayerCredentialRepository playerCredentialRepository,
            final PlayerIdentityRepository playerIdentityRepository,
            final GogomayaValidationService validationService,
            final PlayerRegistrationServerService playerRegistrationService,
            final SocialConnectionDataAdapter socialConnectionDataAdapter) {
        this.playerCredentialRepository = checkNotNull(playerCredentialRepository);
        this.playerIdentityRepository = checkNotNull(playerIdentityRepository);
        this.validationService = checkNotNull(validationService);
        this.playerRegistrationService = checkNotNull(playerRegistrationService);
        this.socialConnectionDataAdapter = checkNotNull(socialConnectionDataAdapter);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = PlayerWebMapping.PLAYER_REGISTRATION_LOGIN, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PlayerIdentity login(@RequestBody PlayerCredential playerCredentials) {
        // Step 1. Fetch saved player credentials
        PlayerCredential fetchedCredentials = playerCredentialRepository.findByEmail(playerCredentials.getEmail());
        // Step 2. If there is no such credentials, than user is unregistered
        if (fetchedCredentials == null)
            throw GogomayaException.fromError(GogomayaError.EmailOrPasswordIncorrect);
        // Step 3. Compare passwords
        if (!fetchedCredentials.getPassword().equals(playerCredentials.getPassword()))
            throw GogomayaException.fromError(GogomayaError.EmailOrPasswordIncorrect);
        // Step 4. Everything is fine, return Identity
        return playerIdentityRepository.findOne(fetchedCredentials.getPlayerId());
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = PlayerWebMapping.PLAYER_REGISTRATION_SIGN_IN, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerIdentity createPlayer(@RequestBody final RegistrationRequest registrationRequest) {
        // Step 1. Validating input data prior to any actions
        validationService.validate(registrationRequest);
        validationService.validate(registrationRequest.getPlayerCredential());
        validationService.validate(registrationRequest.getPlayerProfile());
        // Step 2. Registration done through separate registration service
        return playerRegistrationService.register(registrationRequest);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = PlayerWebMapping.PLAYER_REGISTRATION_SOCIAL, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerIdentity createPlayer(@RequestBody SocialConnectionData socialConnectionData) {
        validationService.validate(socialConnectionData);
        // Step 1. Registering player with SocialConnection
        Long playerId = socialConnectionDataAdapter.register(socialConnectionData);
        // Step 2. Fetch Player identity information
        return playerIdentityRepository.findOne(playerId);
    }

}
