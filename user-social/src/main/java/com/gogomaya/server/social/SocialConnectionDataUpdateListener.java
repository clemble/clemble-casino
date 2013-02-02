package com.gogomaya.server.social;

import java.util.Set;

import javax.inject.Inject;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import com.gogomaya.server.user.SocialConnectionData;
import com.google.common.collect.ImmutableSet;

public class SocialConnectionDataUpdateListener implements MessageListener {

    @Inject
    final private UsersConnectionRepository usersConnectionRepository;

    @Inject
    final private ConnectionFactoryLocator connectionFactoryLocator;

    public SocialConnectionDataUpdateListener(final UsersConnectionRepository usersConnectionRepository, final ConnectionFactoryLocator connectionFactoryLocator){
        this.usersConnectionRepository = usersConnectionRepository;
        this.connectionFactoryLocator = connectionFactoryLocator;
    }
    
    @Override
    public void onMessage(Message message) {
        // Step 1. Converting data to appropriate message presentation
        SocialConnectionData socialConnectionData = (SocialConnectionData) SerializationUtils.deserialize(message.getBody());
        // Step 2. Converting SocialConnectionData to ConnectionData in accordance with the provider
        ConnectionData connectionData = SocialAdapter.getSocialAdapter(socialConnectionData.getProviderId()).toConnectionData(socialConnectionData);
        // Step 3. Creating connection appropriate for the provided data
        Connection<?> connection = connectionFactoryLocator.getConnectionFactory(socialConnectionData.getProviderId()).createConnection(connectionData);
        // Step 4. Checking if the provided connection was valid
        if (connection.test()) {
            // Step 5. Retrieving associated user identifiers
            Set<String> userIds = usersConnectionRepository.findUserIdsConnectedTo(socialConnectionData.getProviderId(),
                    ImmutableSet.<String> of(socialConnectionData.getProviderUserId()));
            if (userIds.size() == 0 || userIds.size() > 1)
                throw new IllegalArgumentException("This should never rich here");
            // Step 6. Updating Connections of the user with the new Data
            String userId = userIds.iterator().next();
            ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(userId);
            connectionRepository.updateConnection(connection);
        }
    }

}
