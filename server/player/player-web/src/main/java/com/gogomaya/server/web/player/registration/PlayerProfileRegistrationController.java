package com.gogomaya.server.web.player.registration;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.SocialConnectionData;
import com.gogomaya.server.player.registration.PlayerProfileRegistrationServerService;
import com.gogomaya.web.mapping.WebMapping;
import com.gogomaya.web.player.PlayerWebMapping;

@Controller
public class PlayerProfileRegistrationController {

    final private PlayerProfileRegistrationServerService playerProfileRegistrationServerService;

    public PlayerProfileRegistrationController(final PlayerProfileRegistrationServerService playerProfileRegistrationServerService) {
        this.playerProfileRegistrationServerService = checkNotNull(playerProfileRegistrationServerService);
    }

    @RequestMapping(method = RequestMethod.POST, value = PlayerWebMapping.PLAYER_PROFILE_REGISTRATION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerProfile createPlayerProfile(@RequestBody final PlayerProfile playerProfile) {
        return playerProfileRegistrationServerService.createPlayerProfile(playerProfile);
    }

    @RequestMapping(method = RequestMethod.POST, value = PlayerWebMapping.PLAYER_PROFILE_REGISTRATION_SOCIAL, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerProfile createPlayerProfile(@RequestBody SocialConnectionData socialConnectionData) {
        return playerProfileRegistrationServerService.createPlayerProfile(socialConnectionData);
    }

}
