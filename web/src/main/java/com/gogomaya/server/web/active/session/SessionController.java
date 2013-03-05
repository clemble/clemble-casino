package com.gogomaya.server.web.active.session;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;

import com.gogomaya.server.game.session.GameSession;
import com.gogomaya.server.player.PlayerSession;
import com.gogomaya.server.player.security.PlayerCredential;
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
        
        PlayerSession playerSession = new PlayerSession()
    }
}
