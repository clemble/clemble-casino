package com.gogomaya.server.web.registration;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.player.SocialConnectionData;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.security.PlayerIdentityRepository;
import com.gogomaya.server.social.SocialConnectionDataAdapter;

@Controller
public class RegistrationSocialConnectionController {

    final private SocialConnectionDataAdapter socialConnectionDataAdapter;

    final private PlayerIdentityRepository playerIdentityRepository;

    final private GogomayaValidationService validationService;

    public RegistrationSocialConnectionController(final SocialConnectionDataAdapter socialConnectionDataAdapter, final PlayerIdentityRepository playerIdentityRepository, final GogomayaValidationService validationService) {
        this.socialConnectionDataAdapter = checkNotNull(socialConnectionDataAdapter);
        this.playerIdentityRepository = checkNotNull(playerIdentityRepository);
        this.validationService = checkNotNull(validationService);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registration/social", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody PlayerIdentity createUser(@RequestBody SocialConnectionData socialConnectionData) {
        validationService.validate(socialConnectionData);
        // Step 1. Registering player with SocialConnection
        Long playerId = socialConnectionDataAdapter.register(socialConnectionData);
        // Step 2. Fetch Player identity information
        return playerIdentityRepository.findOne(playerId);
    }

}
