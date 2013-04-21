package com.gogomaya.server.web.registration;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.error.GogomayaError.Code;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.PlayerProfileRepository;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerCredentialRepository;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.security.PlayerIdentityRepository;
import com.gogomaya.server.player.web.RegistrationRequest;

@Controller
public class RegistrationSignInContoller {

    @Inject
    final private PlayerProfileRepository playerProfileRepository;

    @Inject
    final private PlayerCredentialRepository playerCredentialRepository;

    @Inject
    final private PlayerIdentityRepository playerIdentityRepository;
    
    @Inject
    final private GogomayaValidationService validationService;

    public RegistrationSignInContoller(
            final PlayerProfileRepository gamerProfileRepository,
            final PlayerCredentialRepository playerCredentialRepository,
            final PlayerIdentityRepository playerIdentityRepository,
            final GogomayaValidationService validationService) {
        this.playerProfileRepository = checkNotNull(gamerProfileRepository);
        this.playerCredentialRepository = checkNotNull(playerCredentialRepository);
        this.playerIdentityRepository = checkNotNull(playerIdentityRepository);
        this.validationService = checkNotNull(validationService);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registration/signin", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody PlayerIdentity createUser(@RequestBody final RegistrationRequest playerRegistrationRequest) {
        validationService.validate(playerRegistrationRequest);
        PlayerCredential playerCredentials = playerRegistrationRequest.getPlayerCredential();
        validationService.validate(playerCredentials);
        // Step 1. Fetch associated player credentials
        PlayerCredential fetchedCredentials = playerCredentialRepository.findByEmail(playerCredentials.getEmail());
        // Step 2. If there is such credentials, than user already registered
        if (fetchedCredentials != null) {
            // Step 2.1 If the password is the same, just return identity to the user
            if (playerCredentials.getPassword().equals(fetchedCredentials.getPassword())) {
                return playerIdentityRepository.findOne(fetchedCredentials.getPlayerId());
            } else {
                // Step 2.2 If password does not match this is an error
                throw GogomayaException.create(Code.EMAIL_ALREADY_REGISTERED_CODE);
            }
        }
        // Step 3. Create new profile
        PlayerProfile playerProfile = playerRegistrationRequest.getPlayerProfile();
        validationService.validate(playerCredentials);
        playerProfile = playerProfileRepository.saveAndFlush(playerProfile);
        // Step 4. Create new credentials
        playerCredentials.setPlayerId(playerProfile.getPlayerId());
        playerCredentials = playerCredentialRepository.saveAndFlush(playerCredentials);
        // Step 5. Create new identity
        PlayerIdentity playerIdentity = new PlayerIdentity().setPlayerId(playerProfile.getPlayerId()).setSecret(UUID.randomUUID().toString());
        playerIdentity = playerIdentityRepository.saveAndFlush(playerIdentity);
        // Step 6. Returning player identity
        return playerIdentity;
    }
}
