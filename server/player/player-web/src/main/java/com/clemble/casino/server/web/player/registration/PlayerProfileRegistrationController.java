package com.clemble.casino.server.web.player.registration;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.server.player.registration.PlayerProfileRegistrationServerService;
import com.clemble.casino.web.mapping.WebMapping;
import com.clemble.casino.web.player.PlayerWebMapping;

@Controller
public class PlayerProfileRegistrationController implements PlayerProfileRegistrationServerService{

    final private PlayerProfileRegistrationServerService playerProfileRegistrationServerService;

    public PlayerProfileRegistrationController(final PlayerProfileRegistrationServerService playerProfileRegistrationServerService) {
        this.playerProfileRegistrationServerService = checkNotNull(playerProfileRegistrationServerService);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = PlayerWebMapping.PLAYER_PROFILE_REGISTRATION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody PlayerProfile createPlayerProfile(@RequestBody final PlayerProfile playerProfile) {
        return playerProfileRegistrationServerService.createPlayerProfile(playerProfile);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = PlayerWebMapping.PLAYER_PROFILE_REGISTRATION_SOCIAL, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody PlayerProfile createPlayerProfile(@RequestBody SocialConnectionData socialConnectionData) {
        return playerProfileRegistrationServerService.createPlayerProfile(socialConnectionData);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = PlayerWebMapping.PLAYER_PROFILE_REGISTRATION_GRANT, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody PlayerProfile createPlayerProfile(@RequestBody SocialAccessGrant accessGrant) {
        return playerProfileRegistrationServerService.createPlayerProfile(accessGrant);
    }

}
