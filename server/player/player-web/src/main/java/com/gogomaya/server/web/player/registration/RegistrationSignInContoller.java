package com.gogomaya.server.web.player.registration;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.player.registration.PlayerRegistrationService;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.web.RegistrationRequest;
import com.gogomaya.server.web.mapping.PlayerWebMapping;

@Controller
public class RegistrationSignInContoller {

    final private PlayerRegistrationService playerRegistrationService;

    final private GogomayaValidationService validationService;

    public RegistrationSignInContoller(final PlayerRegistrationService playerRegistrationService, final GogomayaValidationService validationService) {
        this.validationService = checkNotNull(validationService);
        this.playerRegistrationService = checkNotNull(playerRegistrationService);
    }

    @RequestMapping(method = RequestMethod.POST, value = PlayerWebMapping.PLAYER_REGISTRATION_SIGN_IN, produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerIdentity createUser(@RequestBody final RegistrationRequest registrationRequest) {
        // Step 1. Validating input data prior to any actions
        validationService.validate(registrationRequest);
        validationService.validate(registrationRequest.getPlayerCredential());
        validationService.validate(registrationRequest.getPlayerProfile());
        // Step 2. Registration done through separate registration service
        return playerRegistrationService.register(registrationRequest);
    }
}
