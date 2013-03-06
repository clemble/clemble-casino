package com.gogomaya.server.web.active.session;

import org.springframework.stereotype.Controller;

import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.web.active.PlayerIdentityVerificationService;

@Controller
public class SessionController {

    final private PlayerIdentityVerificationService identityVerificationService;

    public SessionController(PlayerIdentityVerificationService identityVerificationService) {
        this.identityVerificationService = identityVerificationService;
    }

    public void create(PlayerIdentity playerIdentity){
        identityVerificationService.verify(playerIdentity);
    }
}
