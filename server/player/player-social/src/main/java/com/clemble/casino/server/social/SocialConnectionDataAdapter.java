package com.clemble.casino.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.player.SocialConnectionData;
import com.google.common.collect.ImmutableSet;

public class SocialConnectionDataAdapter {

    final private ConnectionFactoryLocator connectionFactoryLocator;

    final private UsersConnectionRepository usersConnectionRepository;

    final private SocialConnectionAdapterRegistry socialAdapterRegistry;

    public SocialConnectionDataAdapter(final ConnectionFactoryLocator connectionFactoryLocator,
            final UsersConnectionRepository usersConnectionRepository,
            final SocialConnectionAdapterRegistry socialAdapterRegistry) {
        this.connectionFactoryLocator = checkNotNull(connectionFactoryLocator);
        this.usersConnectionRepository = checkNotNull(usersConnectionRepository);
        this.socialAdapterRegistry = checkNotNull(socialAdapterRegistry);
    }

    public String register(SocialConnectionData socialConnectionData) {
        // Step 1. Sanity check
        if (socialConnectionData == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionInvalid);
        // Step 2. Checking if user already exists
        String player = null;

        Set<String> existingUsers = usersConnectionRepository.findUserIdsConnectedTo(socialConnectionData.getProviderId(),
                ImmutableSet.<String> of(socialConnectionData.getProviderUserId()));
        if (existingUsers.size() == 1) {
            // Step 3. Converting SocialConnectionData to ConnectionData in accordance with the provider
            ConnectionData connectionData = socialAdapterRegistry.getSocialAdapter(socialConnectionData.getProviderId()).toConnectionData(socialConnectionData);
            // Step 4. Creating connection appropriate for the provided data
            Connection<?> connection = connectionFactoryLocator.getConnectionFactory(socialConnectionData.getProviderId()).createConnection(connectionData);
            // Step 5. Checking if the provided connection was valid
            if (connection.test()) {
                // Step 6. Retrieving associated user identifiers
                player = existingUsers.iterator().next();
                ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(player);
                connectionRepository.updateConnection(connection);
            } else {
                throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionInvalid);
            }
        } else if (existingUsers.size() == 0) {
            // Step 2. Converting SocialConnectionData to ConnectionData in accordance with the provider
            ConnectionData connectionData = socialAdapterRegistry.getSocialAdapter(socialConnectionData.getProviderId()).toConnectionData(socialConnectionData);
            // Step 3. Creating connection appropriate for the provided
            Connection<?> connection = connectionFactoryLocator.getConnectionFactory(socialConnectionData.getProviderId()).createConnection(connectionData);
            // Step 4. Saving connection in the Database
            // This is done by calling UserConnectionRespository, which invokes ConnectionSignUp if there is no user with provided Connection
            // So The new user will be created and added to the DB, the Id will be actually generated by JdbcUsersConnectionRepository
            // Check that this logic remains intact
            player = usersConnectionRepository.findUserIdsWithConnection(connection).iterator().next();
        } else {
            throw ClembleCasinoException.fromError(ClembleCasinoError.ServerCriticalError);
        }
        return player;
    }

    public SocialConnectionData add(String playerId, SocialConnectionData socialConnectionData) {
        // Step 1. Sanity check
        if (socialConnectionData == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionInvalid);
        // Step 2. Creating connection to Social network
        ConnectionData connectionData = socialAdapterRegistry.getSocialAdapter(socialConnectionData.getProviderId()).toConnectionData(socialConnectionData);
        Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId()).createConnection(connectionData);
        // Step 3. Checking user is valid
        Set<String> existingUsers = usersConnectionRepository.findUserIdsConnectedTo(socialConnectionData.getProviderId(), ImmutableSet.<String> of(socialConnectionData.getProviderUserId()));
        if (existingUsers.size() == 1) {
            String existingUser = existingUsers.iterator().next();
            if (!existingUser.equals(playerId)) {
                throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionAlreadyRegistered);
            }
        }
        // Step 4. Adding connection to player connection repository
        usersConnectionRepository.createConnectionRepository(playerId).addConnection(connection);
        // Step 5. Returning original social connection data
        return socialConnectionData;
    }
}
