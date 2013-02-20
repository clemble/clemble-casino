package com.gogomaya.server.web.registration;

import static com.google.common.base.Preconditions.checkNotNull;

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
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerCredentialRepository;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.security.PlayerIdentityRepository;

@Controller
public class RegistrationLoginController {

    final private PlayerCredentialRepository playerCredentialRepository;

    final private PlayerIdentityRepository playerIdentityRepository;

    final private GogomayaValidationService validationService;

    public RegistrationLoginController(final PlayerCredentialRepository playerCredentialRepository, final PlayerIdentityRepository playerIdentityRepository, final GogomayaValidationService validationService) {
        this.playerCredentialRepository = checkNotNull(playerCredentialRepository);
        this.playerIdentityRepository = checkNotNull(playerIdentityRepository);
        this.validationService = checkNotNull(validationService);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registration/login", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody PlayerIdentity createUser(@RequestBody PlayerCredential playerCredentials) {
        // Step 0. Validation check
        validationService.validate(playerCredentials);
        // Step 1. Fetch saved player credentials
        PlayerCredential fetchedCredentials = playerCredentialRepository.findByEmail(playerCredentials.getEmail());
        // Step 2. If there is no such credentials, than user is unregistered
        if (fetchedCredentials == null)
            throw GogomayaException.create(Code.EMAIL_NOT_REGISTERED_CODE);
        // Step 3. Compare passwords
        if (!fetchedCredentials.getPassword().equals(playerCredentials.getPassword()))
            throw GogomayaException.create(Code.PASSWORD_IS_INCORRECT_CODE);
        // Step 4. Everything is fine, return Identity
        return playerIdentityRepository.findOne(fetchedCredentials.getPlayerId());
    }

}
