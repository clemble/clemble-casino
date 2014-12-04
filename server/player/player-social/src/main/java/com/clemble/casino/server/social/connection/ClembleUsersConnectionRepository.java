package com.clemble.casino.server.social.connection;

import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by mavarazy on 12/4/14.
 */
public class ClembleUsersConnectionRepository implements UsersConnectionRepository {

    final private UsersConnectionRepository delegate;
    final private SystemNotificationService notificationService;

    public ClembleUsersConnectionRepository(UsersConnectionRepository delegate, SystemNotificationService notificationService) {
        this.delegate = delegate;
        this.notificationService = notificationService;
    }

    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        return delegate.findUserIdsWithConnection(connection);
    }

    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        return delegate.findUserIdsConnectedTo(providerId, providerUserIds);
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        ConnectionRepository connectionRepository = delegate.createConnectionRepository(userId);
        return new ClembleConnectionRepository(userId, connectionRepository, notificationService);
    }
}
