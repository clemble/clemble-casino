package com.gogomaya.server.web.player.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.repository.player.PlayerProfileRepository;
import com.gogomaya.server.web.mapping.PlayerWebMapping;

@Controller
public class PlayerProfileController {
    final private PlayerProfileRepository profileRepository;

    public PlayerProfileController(PlayerProfileRepository playerProfileRepository) {
        this.profileRepository = checkNotNull(playerProfileRepository);
    }

    @RequestMapping(method = RequestMethod.GET, value = PlayerWebMapping.PLAYER_PROFILE, produces = "application/json")
    public @ResponseBody
    PlayerProfile get(@RequestHeader("playerId") long requestor, @PathVariable("player") long playerId) {
        return profileRepository.findOne(playerId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = PlayerWebMapping.PLAYER_PROFILE, produces = "application/json")
    public @ResponseBody
    PlayerProfile put(@RequestHeader("playerId") long requestor, @PathVariable("player") long playerId, @RequestBody PlayerProfile playerProfile) {
        // Step 1. Sanity check
        if (requestor != playerId)
            throw GogomayaException.fromError(GogomayaError.PlayerNotProfileOwner);
        if (playerProfile == null || playerProfile.getPlayerId() != 0 && playerProfile.getPlayerId() != playerId)
            throw GogomayaException.fromError(GogomayaError.PlayerNotProfileOwner);
        playerProfile.setPlayerId(playerId);
        // Step 2. Updating Profile
        return profileRepository.saveAndFlush(playerProfile);
    }

}
