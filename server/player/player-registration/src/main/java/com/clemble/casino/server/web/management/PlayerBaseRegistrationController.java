package com.clemble.casino.server.web.management;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.player.client.ClientDetails;
import com.clemble.casino.player.client.RSAKeySecretFormat;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerBaseRegistrationService;
import com.clemble.casino.player.service.PlayerManualRegistrationService;
import com.clemble.casino.player.web.PlayerBaseRegistrationRequest;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.casino.server.event.SystemPlayerProfileRegistered;
import com.clemble.casino.server.player.PlayerIdGenerator;
import com.clemble.casino.server.player.security.PlayerTokenFactory;
import com.clemble.casino.server.repository.player.PlayerCredentialRepository;
import com.clemble.casino.web.mapping.WebMapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth.common.signature.RSAKeySecret;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.clemble.casino.web.player.PlayerWebMapping.REGISTRATION_BASIC_LOGIN;
import static com.clemble.casino.web.player.PlayerWebMapping.REGISTRATION_BASIC_PROFILE;
import static com.clemble.casino.web.player.PlayerWebMapping.REGISTRATION_LOGIN;

/**
 * Created by mavarazy on 7/28/14.
 */
@Controller
public class PlayerBaseRegistrationController implements PlayerBaseRegistrationService {

    final private ClembleConsumerDetails DEFAULT_DETAILS = new ClembleConsumerDetails("DEFAULT", "web", null, null,null);

    final private PlayerManualRegistrationService manualRegistrationService;

    public PlayerBaseRegistrationController(PlayerManualRegistrationService manualRegistrationService) {
        this.manualRegistrationService = manualRegistrationService;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = REGISTRATION_BASIC_LOGIN, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody PlayerToken login(@RequestBody PlayerCredential playerCredentials) {
        // Step 1. Propagating request
        return manualRegistrationService.login(new PlayerLoginRequest(DEFAULT_DETAILS, playerCredentials));
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = REGISTRATION_BASIC_PROFILE, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody PlayerToken register(@RequestBody PlayerBaseRegistrationRequest registrationRequest) {
        return manualRegistrationService.createPlayer(new PlayerRegistrationRequest(DEFAULT_DETAILS, registrationRequest.getPlayerCredential(), registrationRequest.getPlayerProfile()));
    }

}
