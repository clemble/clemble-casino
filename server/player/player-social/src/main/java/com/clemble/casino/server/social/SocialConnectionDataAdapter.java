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
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;
import com.clemble.casino.server.event.player.SystemPlayerSocialAddedEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.google.common.collect.ImmutableSet;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

public class SocialConnectionDataAdapter implements SignInAdapter {

    final private ConnectionFactoryLocator connectionFactoryLocator;
    final private UsersConnectionRepository usersConnectionRepository;
    final private SocialConnectionAdapterRegistry socialAdapterRegistry;
    final private SystemNotificationService systemNotificationService;

    public SocialConnectionDataAdapter(
            final ConnectionFactoryLocator connectionFactoryLocator,
            final UsersConnectionRepository usersConnectionRepository,
            final SocialConnectionAdapterRegistry socialAdapterRegistry,
            final SystemNotificationService systemNotificationService) {
        this.connectionFactoryLocator = checkNotNull(connectionFactoryLocator);
        this.usersConnectionRepository = checkNotNull(usersConnectionRepository);
        this.socialAdapterRegistry = checkNotNull(socialAdapterRegistry);
        this.systemNotificationService = checkNotNull(systemNotificationService);
    }

    @Override
    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        return register(connection.createData()).getPlayer();
    }

    public SocialConnection register(SocialAccessGrant accessGrant) {
        // Step 1. Sanity check
        if (accessGrant == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionInvalid);
        // Step 2. Creating ConnectionData
        ConnectionData connectionData = socialAdapterRegistry.getSocialAdapter(accessGrant.getProvider()).toConnectionData(accessGrant);
        // Step 3. Registering with fetched Connection data
        return register(connectionData);
    }

    public SocialConnection register(SocialConnectionData socialConnectionData) {
        // Step 1. Sanity check
        if (socialConnectionData == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionInvalid);
        // Step 3. Converting SocialConnectionData to ConnectionData in accordance with the provider
        ConnectionData connectionData = socialAdapterRegistry.getSocialAdapter(socialConnectionData.getProviderId()).toConnectionData(socialConnectionData);
        // Step 4. Registering with fetched connection data
        return register(connectionData);
    }

    private SocialConnection register(ConnectionData connectionData) {
        SocialConnection socialConnection = null;
        // Step 1. Checking if user already exists
        Set<String> existingUsers = usersConnectionRepository.findUserIdsConnectedTo(connectionData.getProviderId(), ImmutableSet.<String> of(connectionData.getProviderUserId()));
        if (existingUsers.size() == 1) {
            // Step 2. Creating connection appropriate for the provided data
            Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId()).createConnection(connectionData);
            // Step 5. Checking if the provided connection was valid
            // TODO need to check that this access was provided for this application
            if (connection.test()) {
                // Step 6. Retrieving associated user identifiers
                String player = existingUsers.iterator().next();
                socialConnection = new SocialConnection(player, connection);
                ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(player);
                connectionRepository.updateConnection(connection);
            } else {
                throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionInvalid);
            }
        } else if (existingUsers.size() == 0) {
            // Step 3. Creating connection appropriate for the provided
            Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId()).createConnection(connectionData);
            // Step 4. Saving connection in the Database
            // This is done by calling UserConnectionRespository, which invokes ConnectionSignUp if there is no user with provided Connection
            // So The new user will be created and added to the DB, the Id will be actually generated by JdbcUsersConnectionRepository
            // Check that this logic remains intact
            String player = usersConnectionRepository.findUserIdsWithConnection(connection).iterator().next();
            socialConnection = new SocialConnection(player, connection);
            // Step 5. Sending request to update user connections
            systemNotificationService.send(new SystemPlayerSocialAddedEvent(player, connection.getKey()));
        } else {
            throw ClembleCasinoException.fromError(ClembleCasinoError.ServerCriticalError);
        }
        return socialConnection;
    }

    public SocialConnectionData add(String player, SocialConnectionData socialConnectionData) {
        // Step 1. Sanity check
        if (socialConnectionData == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionInvalid, player);
        // Step 2. Creating connection to Social network
        ConnectionData connectionData = socialAdapterRegistry.getSocialAdapter(socialConnectionData.getProviderId()).toConnectionData(socialConnectionData);
        Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId()).createConnection(connectionData);
        // Step 3. Checking user is valid
        Set<String> existingUsers = usersConnectionRepository.findUserIdsConnectedTo(socialConnectionData.getProviderId(), ImmutableSet.<String> of(socialConnectionData.getProviderUserId()));
        if (existingUsers.size() == 1) {
            String existingUser = existingUsers.iterator().next();
            if (!existingUser.equals(player)) {
                throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionAlreadyRegistered);
            }
        }
        // Step 4. Adding connection to player connection repository
        usersConnectionRepository.createConnectionRepository(player).addConnection(connection);
        // Step 5. Sending request to update user connections
        systemNotificationService.send(new SystemPlayerSocialAddedEvent(player, connection.getKey()));
        // Step 6. Returning original social connection data
        return socialConnectionData;
    }
}
