package com.clemble.casino.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionSignUp;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.player.PlayerIdGenerator;
import com.clemble.casino.server.player.social.PlayerSocialNetwork;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;

public class SocialProfileConnectionSignUp implements ConnectionSignUp {

    final private PlayerIdGenerator idGenerator;
    final private PlayerProfileRepository profileRepository;
    final private PlayerSocialNetworkRepository socialNetworkRepository;
    final private SocialConnectionAdapterRegistry socialAdapterRegistry;

    public SocialProfileConnectionSignUp(final PlayerIdGenerator idGenerator,
            final PlayerProfileRepository profileRepository,
            final SocialConnectionAdapterRegistry socialAdapterRegistry,
            final PlayerSocialNetworkRepository socialNetworkRepository) {
        this.idGenerator = checkNotNull(idGenerator);
        this.profileRepository = checkNotNull(profileRepository);
        this.socialAdapterRegistry = checkNotNull(socialAdapterRegistry);
        this.socialNetworkRepository = checkNotNull(socialNetworkRepository);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String execute(Connection<?> connection) {
        // Step 1. Checking provided Connection<?> valid
        if (connection == null)
            throw new IllegalArgumentException("No Connection defined.");
        // Step 2. Retrieving SocialAdapter for the Connection
        ConnectionKey connectionKey = connection.getKey();
        SocialConnectionAdapter socialAdapter = socialAdapterRegistry.getSocialAdapter(connectionKey.getProviderId());
        if (socialAdapter == null)
            throw new IllegalArgumentException("No SocialAdapter exists for Connection");
        // Step 3. Generating gamer profile based on SocialConnection
        PlayerProfile playerProfile = socialAdapter.fetchPlayerProfile(connection.getApi());
        playerProfile.setPlayer(idGenerator.newId());
        playerProfile = profileRepository.save(playerProfile);
        // Step 4. Fetching player social network
        // Step 1. Creating owned social network
        PlayerSocialNetwork socialNetwork = new PlayerSocialNetwork()
            .addOwned(playerProfile.getSocialConnections())
            .setPlayer(playerProfile.getPlayer());
        socialNetworkRepository.save(socialNetwork);
        // Step 5. Returning playerIdentity as a result
        return String.valueOf(playerProfile.getPlayer());
    }

}
