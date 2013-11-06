package com.clemble.casino.server.web.player;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.service.PlayerProfileService;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.web.mapping.WebMapping;
import com.clemble.casino.web.player.PlayerWebMapping;
import com.wordnik.swagger.annotations.Api;

@Api(value = "player")
@Controller
public class PlayerProfileController implements PlayerProfileService {

    final private PlayerProfileRepository profileRepository;

    public PlayerProfileController(PlayerProfileRepository playerProfileRepository) {
        this.profileRepository = checkNotNull(playerProfileRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PlayerWebMapping.PLAYER_PROFILE, produces = WebMapping.PRODUCES)
    public @ResponseBody PlayerProfile getPlayerProfile(@PathVariable("playerId") String playerId) {
        // Step 1. Fetching playerProfile
        PlayerProfile playerProfile = profileRepository.findOne(playerId);
        // Step 2. Checking profile
        if(playerProfile == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerProfileDoesNotExists);
        // Step 3. Returning profile
        return playerProfile;
    }

    @Override
    @RequestMapping(method = RequestMethod.PUT, value = PlayerWebMapping.PLAYER_PROFILE, produces = WebMapping.PRODUCES)
    public @ResponseBody PlayerProfile updatePlayerProfile(@PathVariable("playerId") String player, @RequestBody PlayerProfile playerProfile) {
        // Step 1. Sanity check
        if (playerProfile == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerProfileInvalid);
        if (!playerProfile.getPlayer().equals(player))
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerNotProfileOwner);
        playerProfile.setPlayer(player);
        // Step 2. Updating Profile
        return profileRepository.save(playerProfile);
    }

}
