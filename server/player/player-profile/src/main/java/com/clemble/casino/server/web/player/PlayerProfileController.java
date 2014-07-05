package com.clemble.casino.server.web.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.service.PlayerProfileService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.web.mapping.WebMapping;
import com.clemble.casino.web.player.PlayerWebMapping;

@Controller
public class PlayerProfileController implements PlayerProfileService, ExternalController {

    // TODO need a listener, that adds ConnectionKey to PlayerProfile when connection added

    final private PlayerProfileRepository profileRepository;

    public PlayerProfileController(PlayerProfileRepository playerProfileRepository) {
        this.profileRepository = checkNotNull(playerProfileRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PlayerWebMapping.PROFILE_PLAYER, produces = WebMapping.PRODUCES)
    public @ResponseBody PlayerProfile getPlayerProfile(@PathVariable("player") String playerId) {
        // Step 1. Fetching playerProfile
        PlayerProfile playerProfile = profileRepository.findOne(playerId);
        // Step 2. Checking profile
        if (playerProfile == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerProfileDoesNotExists);
        // Step 3. Returning profile
        return playerProfile;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = PlayerWebMapping.PROFILE_PLAYER, produces = WebMapping.PRODUCES)
    public @ResponseBody
    PlayerProfile updatePlayerProfile(@PathVariable("player") String player, @RequestBody PlayerProfile playerProfile) {
        // Step 1. Sanity check
        if (playerProfile == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerProfileInvalid);
        if (!playerProfile.getPlayer().equals(player))
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerNotProfileOwner);
        playerProfile.setPlayer(player);
        // Step 1.1. Checking player does not try to add additional Social Connections
        PlayerProfile existingProfile = profileRepository.findOne(player);
        if (!existingProfile.getSocialConnections().equals(playerProfile.getSocialConnections()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.ProfileSocialCantBeEdited);
        // Step 2. Updating Profile
        return profileRepository.save(playerProfile);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PlayerWebMapping.PROFILE, produces = WebMapping.PRODUCES)
    public @ResponseBody List<PlayerProfile> getPlayerProfile(@RequestParam("player") Collection<String> players) {
        return profileRepository.findAll(players);
    }

}
