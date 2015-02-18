package com.clemble.casino.server.social.listener;

import com.clemble.casino.server.event.share.SystemSharePostEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.social.SocialAdapter;
import com.clemble.casino.server.social.SocialAdapterRegistry;
import org.springframework.social.ApiBinding;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import java.util.Collection;

/**
 * Created by mavarazy on 1/8/15.
 */
public class SystemSharePostEventListener implements SystemEventListener<SystemSharePostEvent> {

    final private SocialAdapterRegistry socialAdapterRegistry;
    final private UsersConnectionRepository usersConnectionRepository;


    public SystemSharePostEventListener(
        SocialAdapterRegistry socialAdapterRegistry,
        UsersConnectionRepository usersConnectionRepository
    ) {
        this.socialAdapterRegistry = socialAdapterRegistry;
        this.usersConnectionRepository = usersConnectionRepository;
    }

    @Override
    public void onEvent(SystemSharePostEvent event) {
        // Step 1. Finding appropriate SocialConnectionAdapter
        SocialAdapter socialAdapter = socialAdapterRegistry.getSocialAdapter(event.getProviderId());
        // Step 2. Fetching connection
        ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(event.getPlayer());
        Connection<?> connection = connectionRepository.findConnections(event.getProviderId().name()).get(0);
        // Step 3. Fetching PlayerSocialNetwork and existing connections
        socialAdapter.share(connection.getKey().getProviderId(), event.getPost().getState().getGoal(), (ApiBinding) connection.getApi());
    }

    @Override
    public String getChannel() {
        return SystemSharePostEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemSharePostEvent.CHANNEL + " > player:social";
    }
}
