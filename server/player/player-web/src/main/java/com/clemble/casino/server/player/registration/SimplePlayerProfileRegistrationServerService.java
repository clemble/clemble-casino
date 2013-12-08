package com.clemble.casino.server.player.registration;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.social.oauth2.AccessGrant;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.SocialPlayerProfile;
import com.clemble.casino.server.player.registration.PlayerProfileRegistrationServerService;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.social.SocialConnectionDataAdapter;

public class SimplePlayerProfileRegistrationServerService implements PlayerProfileRegistrationServerService {

    final private PlayerProfileRepository playerProfileRepository;
    final private SocialConnectionDataAdapter socialConnectionDataAdapter;
    final private ClembleCasinoValidationService validationService;

    public SimplePlayerProfileRegistrationServerService(final ClembleCasinoValidationService validationService,
            final PlayerProfileRepository playerProfileRepository,
            final SocialConnectionDataAdapter socialConnectionDataAdapter) {
        this.validationService = checkNotNull(validationService);
        this.playerProfileRepository = checkNotNull(playerProfileRepository);
        this.socialConnectionDataAdapter = checkNotNull(socialConnectionDataAdapter);
    }

    @Override
    public PlayerProfile createPlayerProfile(final PlayerProfile playerProfile) {
        if (playerProfile instanceof SocialPlayerProfile)
            throw ClembleCasinoException.fromError(ClembleCasinoError.ProfileInvalid);
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

    @Override
    public PlayerProfile createPlayerProfile(SocialAccessGrant accessGrant) {
        validationService.validate(accessGrant);
        // Step 1. Registering player with SocialConnection
        String player = socialConnectionDataAdapter.register(accessGrant);
        // Step 2. Fetch Player identity information
        return playerProfileRepository.findOne(player);
    }

}
