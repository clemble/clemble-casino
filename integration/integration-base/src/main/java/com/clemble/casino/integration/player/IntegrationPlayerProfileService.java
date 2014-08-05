package com.clemble.casino.integration.player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.service.PlayerProfileService;
import com.clemble.casino.server.profile.controller.PlayerProfileServiceController;

public class IntegrationPlayerProfileService implements PlayerProfileService {

    /**
     * Generated 15/12/13
     */
    private static final long serialVersionUID = 2044631083380608080L;

    final private String player;
    final private PlayerProfileServiceController profileService;

    public IntegrationPlayerProfileService(String player, PlayerProfileServiceController profileService) {
        this.player = player;
        this.profileService = profileService;
    }

    @Override
    public PlayerProfile myProfile() {
        return profileService.myProfile(player);
    }

    @Override
    public PlayerProfile updateProfile(PlayerProfile playerProfile) {
        return profileService.updateProfile(player, playerProfile);
    }

    @Override
    public PlayerProfile getProfile(String player) {
        return profileService.getProfile(player);
    }

    @Override
    public List<PlayerProfile> getProfiles(String... players) {
        return getProfiles(Arrays.asList(players));
    }

    @Override
    public List<PlayerProfile> getProfiles(Collection<String> players) {
        return profileService.getProfiles(players);
    }

}
