package com.clemble.casino.server.social.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.server.ExternalController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clemble.casino.social.SocialConnectionData;
import com.clemble.casino.social.service.PlayerSocialProfileServiceContract;
import com.clemble.casino.server.social.SocialConnectionDataAdapter;
import com.clemble.casino.web.mapping.WebMapping;
import static com.clemble.casino.web.player.PlayerWebMapping.*;

public class PlayerSocialProfileController implements PlayerSocialProfileServiceContract, ExternalController {

    final private SocialConnectionDataAdapter socialConnectionDataAdapter;

    public PlayerSocialProfileController(SocialConnectionDataAdapter socialConnectionDataAdapter) {
        this.socialConnectionDataAdapter = checkNotNull(socialConnectionDataAdapter);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = SOCIAL_PLAYER, produces = WebMapping.PRODUCES)
    public @ResponseBody SocialConnectionData add(@PathVariable("player") String playerId, @RequestBody SocialConnectionData socialConnectionData) {
        return socialConnectionDataAdapter.add(playerId, socialConnectionData);
    }

}
