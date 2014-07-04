package com.clemble.casino.server.player.registration;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.server.player.social.PlayerSocialNetwork;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;
import com.clemble.casino.server.social.SocialConnectionDataAdapter;

@Deprecated
public class BasicServerProfileRegistrationService implements ServerProfileRegistrationService {

    final private PlayerProfileRepository playerProfileRepository;
    final private SocialConnectionDataAdapter socialConnectionDataAdapter;
    final private PlayerSocialNetworkRepository socialNetworkRepository;
    final private ClembleCasinoValidationService validationService;

    public BasicServerProfileRegistrationService(
        final ClembleCasinoValidationService validationService,
        final PlayerProfileRepository playerProfileRepository,
        final SocialConnectionDataAdapter socialConnectionDataAdapter,
        final PlayerSocialNetworkRepository socialNetworkRepository) {
        this.validationService = checkNotNull(validationService);
        this.playerProfileRepository = checkNotNull(playerProfileRepository);
        this.socialConnectionDataAdapter = checkNotNull(socialConnectionDataAdapter);
        this.socialNetworkRepository = checkNotNull(socialNetworkRepository);
    }

    @Override
    public PlayerProfile create(PlayerProfile playerProfile) {
        if (playerProfile == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.ProfileInvalid);
        if (playerProfile.getSocialConnections() != null && !playerProfile.getSocialConnections().isEmpty())
            throw ClembleCasinoException.fromError(ClembleCasinoError.ProfileSocialMustBeEmpty);
        // Step 1. Validating input data prior to any actions
        validationService.validate(playerProfile);
        // Step 2. Registration done through separate registration service
        playerProfile = playerProfileRepository.save(playerProfile);
        // Step 3. Creating empty social network
        socialNetworkRepository.save(new PlayerSocialNetwork(playerProfile.getPlayer()));
        // Step 4. Returning created PlayerProfile
        return playerProfile;
    }

    @Override
    public PlayerProfile create(SocialConnectionData socialConnectionData) {
        validationService.validate(socialConnectionData);
        // Step 1. Registering player with SocialConnection
        String player = socialConnectionDataAdapter.register(socialConnectionData);
        // Step 2. Fetch Player identity information
        return playerProfileRepository.findOne(player);
    }

    @Override
    public PlayerProfile create(SocialAccessGrant accessGrant) {
        validationService.validate(accessGrant);
        // Step 1. Registering player with SocialConnection
        String player = socialConnectionDataAdapter.register(accessGrant);
        // Step 2. Fetch Player identity information
        return playerProfileRepository.findOne(player);
    }

}
