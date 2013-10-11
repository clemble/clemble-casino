package com.clemble.casino.server.player.registration;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.error.GogomayaValidationService;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.server.player.registration.PlayerProfileRegistrationServerService;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.social.SocialConnectionDataAdapter;

public class SimplePlayerProfileRegistrationServerService implements PlayerProfileRegistrationServerService {

    final private PlayerProfileRepository playerProfileRepository;
    final private SocialConnectionDataAdapter socialConnectionDataAdapter;
    final private GogomayaValidationService validationService;

    public SimplePlayerProfileRegistrationServerService(final GogomayaValidationService validationService,
            final PlayerProfileRepository playerProfileRepository,
            final SocialConnectionDataAdapter socialConnectionDataAdapter) {
        this.validationService = checkNotNull(validationService);
        this.playerProfileRepository = checkNotNull(playerProfileRepository);
        this.socialConnectionDataAdapter = checkNotNull(socialConnectionDataAdapter);
    }

    @Override
    public PlayerProfile createPlayerProfile(final PlayerProfile playerProfile) {
        // Step 1. Validating input data prior to any actions
        validationService.validate(playerProfile);
        // Step 2. Registration done through separate registration service
        return playerProfileRepository.save(playerProfile);
    }

    @Override
    public PlayerProfile createPlayerProfile(SocialConnectionData socialConnectionData) {
        validationService.validate(socialConnectionData);
        // Step 1. Registering player with SocialConnection
        String player = socialConnectionDataAdapter.register(socialConnectionData);
        // Step 2. Fetch Player identity information
        return playerProfileRepository.findOne(player);
    }

}