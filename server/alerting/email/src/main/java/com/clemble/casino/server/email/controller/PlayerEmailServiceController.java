package com.clemble.casino.server.email.controller;

import com.clemble.casino.player.PlayerEmailWebMapping;
import com.clemble.casino.player.service.PlayerEmailService;
import com.clemble.casino.server.email.service.ServerPlayerEmailService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mavarazy on 12/8/14.
 */
@RestController
public class PlayerEmailServiceController implements PlayerEmailService {

    final private ServerPlayerEmailService playerEmailService;

    public PlayerEmailServiceController(ServerPlayerEmailService playerEmailService) {
        this.playerEmailService = playerEmailService;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PlayerEmailWebMapping.VERIFY)
    public boolean verify(@PathVariable("verificationCode") String verificationCode) {
        return playerEmailService.verify(verificationCode);
    }

}
