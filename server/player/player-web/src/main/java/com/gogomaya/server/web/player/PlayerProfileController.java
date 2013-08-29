package com.gogomaya.server.web.player;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.PlayerProfileService;
import com.gogomaya.server.repository.player.PlayerProfileRepository;
import com.gogomaya.web.mapping.PlayerWebMapping;

@Controller
public class PlayerProfileController implements PlayerProfileService {

    final private PlayerProfileRepository profileRepository;

    public PlayerProfileController(PlayerProfileRepository playerProfileRepository) {
        this.profileRepository = checkNotNull(playerProfileRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PlayerWebMapping.PLAYER_PROFILE, produces = "application/json")
    public @ResponseBody PlayerProfile getPlayerProfile(@PathVariable("playerId") long playerId) {
        // Step 1. Fetching playerProfile
        PlayerProfile playerProfile = profileRepository.findOne(playerId);
        // Step 2. Checking profile
        if(playerProfile == null)
            throw GogomayaException.fromError(GogomayaError.PlayerProfileDoesNotExists);
        // Step 3. Returning profile
        return playerProfile;
    }

    @Override
    @RequestMapping(method = RequestMethod.PUT, value = PlayerWebMapping.PLAYER_PROFILE, produces = "application/json")
    public @ResponseBody PlayerProfile updatePlayerProfile(@PathVariable("playerId") long playerId, @RequestBody PlayerProfile playerProfile) {
        // Step 1. Sanity check
        if (playerProfile == null)
            throw GogomayaException.fromError(GogomayaError.PlayerProfileInvalid);
        if (playerProfile.getPlayerId() != 0 && playerProfile.getPlayerId() != playerId)
            throw GogomayaException.fromError(GogomayaError.PlayerNotProfileOwner);
        playerProfile.setPlayerId(playerId);
        // Step 2. Updating Profile
        return profileRepository.saveAndFlush(playerProfile);
    }

}
