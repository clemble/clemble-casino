package com.clemble.casino.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.event.email.SystemEmailAddedEvent;
import com.clemble.casino.server.event.player.SystemPlayerImageChangedEvent;
import com.clemble.casino.server.event.player.SystemPlayerProfileRegisteredEvent;
import com.clemble.casino.social.SocialProvider;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.ApiBinding;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.google.common.collect.ImmutableSet;

public class SocialConnectionDataAdapter {

    final private static Logger LOG = LoggerFactory.getLogger(SocialConnectionData.class);

    final private ConnectionFactoryLocator connectionFactoryLocator;
    final private UsersConnectionRepository usersConnectionRepository;
    final private SocialAdapterRegistry socialAdapterRegistry;
    final private SystemNotificationService notificationService;

    public SocialConnectionDataAdapter(
            final ConnectionFactoryLocator connectionFactoryLocator,
            final UsersConnectionRepository usersConnectionRepository,
            final SocialAdapterRegistry socialAdapterRegistry,
            final SystemNotificationService notificationService) {
        this.connectionFactoryLocator = checkNotNull(connectionFactoryLocator);
        this.usersConnectionRepository = checkNotNull(usersConnectionRepository);
        this.socialAdapterRegistry = checkNotNull(socialAdapterRegistry);
        this.notificationService = checkNotNull(notificationService);
    }

    public String register(SocialAccessGrant accessGrant) {
        LOG.debug("register with {}", accessGrant);
        // Step 1. Sanity check
        if (accessGrant == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionInvalid);
        // Step 2. Creating ConnectionData
        ConnectionData connectionData = socialAdapterRegistry.getSocialAdapter(accessGrant.getProvider()).toConnectionData(accessGrant);
        LOG.debug("register normalized to {}", connectionData);
        // Step 3. Registering with fetched Connection data
        return register(connectionData);
    }

    public String register(SocialConnectionData socialConnectionData) {
        LOG.debug("register with {}", socialConnectionData);
        // Step 1. Sanity check
        if (socialConnectionData == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionInvalid);
        // Step 3. Converting SocialConnectionData to ConnectionData in accordance with the provider
        ConnectionData connectionData = socialAdapterRegistry.getSocialAdapter(socialConnectionData.getProviderId()).toConnectionData(socialConnectionData);
        LOG.debug("register normalized to {}", connectionData);
        // Step 4. Registering with fetched connection data
        return register(connectionData);
    }

    public String register(ConnectionData connectionData) {
        // Step 1. Checking if user already exists
        LOG.debug("register search for existing users {}", connectionData);
        Set<String> existingUsers = usersConnectionRepository.findUserIdsConnectedTo(connectionData.getProviderId(), ImmutableSet.<String> of(connectionData.getProviderUserId()));
        LOG.debug("register fetched {}", existingUsers);
        if (existingUsers.size() > 1)
            throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionInvalid);
        // This is for signIn through spring - key generated prior to processing, by the time it reaches
        // here user could already exist
        if (existingUsers.size() == 1) {
            String player = existingUsers.iterator().next();
            LOG.debug("register {} verifying connection", player, connectionData);
            // Step 2. Creating connection appropriate for the provided data
            Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId()).createConnection(connectionData);
            // Step 5. Checking if the provided connection was valid
            LOG.debug("register {} connection obtained for {}, testing if it's valid", player, connection.getKey());
            // TODO need to check that this access was provided for this application
            if (connection.test()) {
                LOG.debug("register {} test passed for {}, updating connection", player, connection.getKey());
                // Step 6. Retrieving associated user identifiers
                ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(player);
                connectionRepository.updateConnection(connection);
            } else {
                LOG.error("register {} test FAILED for {}, throwing exception", player, connection.getKey());
                throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionInvalid);
            }
        }
        LOG.error("register new player from {}:{}", connectionData.getProviderId(), connectionData.getProviderUserId());
        // Step 3. Creating connection appropriate for the provided
        Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId()).createConnection(connectionData);
        LOG.error("register connection acquired for {}", connection.getKey());
        // Step 4. Saving connection in the Database
        // This is done by calling UserConnectionRespository, which invokes ConnectionSignUp if there is no user with provided Connection
        // So The new user will be created and added to the DB, the Id will be actually generated by JdbcUsersConnectionRepository
        // Check that this logic remains intact
        // TODO add same verification for connection as above
        String player = usersConnectionRepository.findUserIdsWithConnection(connection).iterator().next();
        LOG.error("register {} created with connection {}", player, connection.getKey());
        // Step 5. Fetching player profile
        SocialProvider provider = SocialProvider.valueOf(connection.getKey().getProviderId());
        SocialAdapter adapter = socialAdapterRegistry.getSocialAdapter(provider);
        ApiBinding api = (ApiBinding) connection.getApi();
        PlayerProfile playerProfile = adapter.fetchPlayerProfile(api);
        playerProfile.setPlayer(player);
        LOG.error("register {} created player profile {}", player, playerProfile);
        notificationService.send(new SystemPlayerProfileRegisteredEvent(playerProfile.getPlayer(), playerProfile));
        // Step 5.1 Sending email notification
        String email = adapter.getEmail(api);
        if (email != null) {
            notificationService.send(new SystemEmailAddedEvent(player, email, true));
        }
        // Step 6. Notifying of added social connection
        Pair<String, String> imageUrl = adapter.toImageUrl(connection);
        LOG.error("register {} updating player picture", player, imageUrl.getLeft());
        if (imageUrl != null) {
            notificationService.send(new SystemPlayerImageChangedEvent(player, imageUrl.getLeft(), imageUrl.getRight()));
        }
        LOG.error("register {} notifying social added to update connections", player, imageUrl.getLeft());
        // Step 9. Returning player profile
        LOG.error("register {} returning", player);
        return player;
    }

    public SocialConnectionData add(String player, SocialConnectionData socialConnectionData) {
        // Step 1. Sanity check
        if (socialConnectionData == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionInvalid, player);
        // Step 2. Creating connection to Social network
        ConnectionData connectionData = socialAdapterRegistry.getSocialAdapter(socialConnectionData.getProviderId()).toConnectionData(socialConnectionData);
        Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId()).createConnection(connectionData);
        // Step 3. Checking user is valid
        Set<String> existingUsers = usersConnectionRepository.findUserIdsConnectedTo(socialConnectionData.getProviderId().name(), ImmutableSet.<String> of(socialConnectionData.getProviderUserId()));
        if (existingUsers.size() == 1) {
            String existingUser = existingUsers.iterator().next();
            if (!existingUser.equals(player)) {
                throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionAlreadyRegistered);
            }
        }
        // Step 4. Adding connection to player connection repository
        usersConnectionRepository.createConnectionRepository(player).addConnection(connection);
        // Step 5. Returning original social connection data
        return socialConnectionData;
    }
}
