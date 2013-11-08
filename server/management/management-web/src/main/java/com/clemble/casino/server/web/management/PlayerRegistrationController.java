package com.clemble.casino.server.web.management;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerRegistrationService;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.casino.player.web.PlayerSocialRegistrationRequest;
import com.clemble.casino.server.player.PlayerIdGenerator;
import com.clemble.casino.server.player.account.PlayerAccountServerService;
import com.clemble.casino.server.player.registration.PlayerProfileRegistrationServerService;
import com.clemble.casino.server.repository.player.PlayerCredentialRepository;
import com.clemble.casino.server.security.ClembleConsumerDetailsService;
import com.clemble.casino.web.management.ManagementWebMapping;
import com.clemble.casino.web.mapping.WebMapping;

@Controller
public class PlayerRegistrationController implements PlayerRegistrationService {

    final private PlayerIdGenerator playerIdentifierGenerator;
    final private PlayerCredentialRepository playerCredentialRepository;
    final private PlayerProfileRegistrationServerService playerProfileRegistrationService;
    final private ClembleCasinoValidationService validationService;
    final private ClembleConsumerDetailsService consumerDetailsService;
    final private PlayerAccountServerService playerAccountServerService;

    public PlayerRegistrationController(final PlayerIdGenerator playerIdentifierGenerator,
            final PlayerProfileRegistrationServerService playerProfileRegistrationService, final PlayerCredentialRepository playerCredentialRepository,
            final ClembleConsumerDetailsService playerIdentityRepository, final ClembleCasinoValidationService validationService,
            final PlayerAccountServerService playerAccountServerService) {
        this.playerIdentifierGenerator = checkNotNull(playerIdentifierGenerator);
        this.playerAccountServerService = checkNotNull(playerAccountServerService);
        this.playerProfileRegistrationService = checkNotNull(playerProfileRegistrationService);
        this.playerCredentialRepository = checkNotNull(playerCredentialRepository);
        this.consumerDetailsService = checkNotNull(playerIdentityRepository);
        this.validationService = checkNotNull(validationService);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = ManagementWebMapping.MANAGEMENT_PLAYER_LOGIN, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PlayerToken login(@RequestBody PlayerLoginRequest loginRequest) {
        PlayerCredential playerCredentials = loginRequest.getPlayerCredential();
        // Step 1. Fetch saved player credentials
        PlayerCredential fetchedCredentials = playerCredentialRepository.findByEmail(playerCredentials.getEmail());
        // Step 2. If there is no such credentials, than user is unregistered
        if (fetchedCredentials == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.EmailOrPasswordIncorrect);
        // Step 3. Compare passwords
        if (!fetchedCredentials.getPassword().equals(playerCredentials.getPassword()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.EmailOrPasswordIncorrect);
        // Step 4. Everything is fine, return Identity
        consumerDetailsService.save(loginRequest.getConsumerDetails());
        return generateToken(fetchedCredentials.getPlayer(), loginRequest.getConsumerDetails());
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = ManagementWebMapping.MANAGEMENT_PLAYER_REGISTRATION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerToken createPlayer(@RequestBody final PlayerRegistrationRequest registrationRequest) {
        // Step 1. Validating input data prior to any actions
        PlayerToken playerIdentity = restoreUser(registrationRequest);
        if (playerIdentity != null)
            return playerIdentity;
        validationService.validate(registrationRequest.getPlayerProfile());
        // Step 2. Creating appropriate PlayerProfile
        PlayerProfile savedProfile = registrationRequest.getPlayerProfile();
        savedProfile.setPlayer(playerIdentifierGenerator.newId());
        savedProfile = playerProfileRegistrationService.createPlayerProfile(savedProfile);
        // Step 3. Registration done through separate registration service
        return register(registrationRequest, savedProfile);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = ManagementWebMapping.MANAGEMENT_PLAYER_REGISTRATION_SOCIAL, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerToken createSocialPlayer(@RequestBody PlayerSocialRegistrationRequest socialRegistrationRequest) {
        // Step 1. Checking if this user already exists
        PlayerToken playerIdentity = restoreUser(socialRegistrationRequest);
        if (playerIdentity != null)
            return playerIdentity;
        // Step 2. Creating appropriate PlayerProfile
        validationService.validate(socialRegistrationRequest.getSocialConnectionData());
        // Step 3. Registering player with SocialConnection
        PlayerProfile playerProfile = playerProfileRegistrationService.createPlayerProfile(socialRegistrationRequest.getSocialConnectionData());
        // Step 4. Register new user and identity
        return register(socialRegistrationRequest, playerProfile);
    }

    private PlayerToken restoreUser(PlayerLoginRequest loginRequest) {
        // Step 1. Checking request is valid
        validationService.validate(loginRequest);
        validationService.validate(loginRequest.getPlayerCredential());
        validationService.validate(loginRequest.getConsumerDetails());
        // Step 2. Checking this is a new user or an old one
        PlayerCredential playerCredentials = loginRequest.getPlayerCredential();
        // Step 3. Fetch associated player credentials
        PlayerCredential fetchedCredentials = playerCredentialRepository.findByEmail(playerCredentials.getEmail());
        // Step 2. If there is such credentials, than user already registered
        if (fetchedCredentials != null) {
            // Step 2.1 If the password is the same, just return identity to the user
            if (playerCredentials.getPassword().equals(fetchedCredentials.getPassword())) {
                return generateToken(fetchedCredentials.getPlayer(), loginRequest.getConsumerDetails());
            } else {
                // Step 2.2 If password does not match this is an error
                throw ClembleCasinoException.fromError(ClembleCasinoError.EmailAlreadyRegistered);
            }
        }
        return null;
    }

    public PlayerToken register(final PlayerLoginRequest loginRequest, final PlayerProfile playerProfile) {
        // Step 0. Registering first wallet
        playerAccountServerService.register(playerProfile);
        // Step 1. Create new credentials
        PlayerCredential playerCredentials = loginRequest.getPlayerCredential().setPlayer(playerProfile.getPlayer());
        playerCredentials = playerCredentialRepository.saveAndFlush(playerCredentials);
        // Step 2. Create new token
        return generateToken(playerProfile.getPlayer(), loginRequest.getConsumerDetails());
    }

    private PlayerToken generateToken(String player, ClembleConsumerDetails consumerDetails) {
        return new PlayerToken(player, "none", "none");
    }

}
