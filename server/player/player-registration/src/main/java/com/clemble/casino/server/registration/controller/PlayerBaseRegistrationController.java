package com.clemble.casino.server.registration.controller;

import com.clemble.casino.security.ClembleConsumerDetails;
import com.clemble.casino.registration.PlayerCredential;
import com.clemble.casino.registration.PlayerToken;
import com.clemble.casino.registration.service.PlayerBaseRegistrationService;
import com.clemble.casino.registration.service.PlayerManualRegistrationService;
import com.clemble.casino.registration.PlayerBaseRegistrationRequest;
import com.clemble.casino.registration.PlayerLoginRequest;
import com.clemble.casino.registration.PlayerRegistrationRequest;
import com.clemble.casino.server.security.PlayerTokenUtils;
import com.clemble.casino.WebMapping;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.clemble.casino.registration.RegistrationWebMapping.*;

/**
 * Created by mavarazy on 7/28/14.
 */
@RestController
public class PlayerBaseRegistrationController implements PlayerBaseRegistrationService {

    final private ClembleConsumerDetails DEFAULT_DETAILS = new ClembleConsumerDetails("DEFAULT", "web", null, null,null);

    final private PlayerTokenUtils tokenUtils;
    final private PlayerManualRegistrationService manualRegistrationService;

    public PlayerBaseRegistrationController(PlayerTokenUtils tokenUtils, PlayerManualRegistrationService manualRegistrationService) {
        this.tokenUtils = tokenUtils;
        this.manualRegistrationService = manualRegistrationService;
    }

    @Override
    public PlayerToken login(PlayerCredential playerCredentials) {
        // Step 1. Propagating request
        return manualRegistrationService.login(new PlayerLoginRequest(DEFAULT_DETAILS, playerCredentials));
    }

    @RequestMapping(method = RequestMethod.POST, value = REGISTRATION_BASIC_LOGIN, produces = WebMapping.PRODUCES)
    public PlayerToken httpLogin(@RequestBody PlayerCredential credentials, HttpServletResponse response) {
        PlayerToken token = login(credentials);
        tokenUtils.updateResponse(token.getPlayer(), response);
        return token;
    }

    @Override
    public PlayerToken register(PlayerBaseRegistrationRequest registrationRequest) {
        return manualRegistrationService.createPlayer(new PlayerRegistrationRequest(DEFAULT_DETAILS, registrationRequest.getPlayerCredential(), registrationRequest.getPlayerProfile()));
    }

    @RequestMapping(method = RequestMethod.POST, value = REGISTRATION_BASIC_PROFILE, produces = WebMapping.PRODUCES)
    public PlayerToken httpRegister(@RequestBody PlayerBaseRegistrationRequest registrationRequest, HttpServletResponse response) {
        PlayerToken token = register(registrationRequest);
        tokenUtils.updateResponse(token.getPlayer(), response);
        return token;
    }

}
