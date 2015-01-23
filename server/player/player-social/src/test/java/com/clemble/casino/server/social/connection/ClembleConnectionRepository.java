package com.clemble.casino.server.social.connection;

import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.notification.SystemNotificationAddEvent;
import com.clemble.casino.server.event.player.SystemPlayerSocialAddedEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * Created by mavarazy on 12/4/14.
 */
public class ClembleConnectionRepository implements ConnectionRepository, PlayerAware {

    final private String player;
    final private ConnectionRepository delegate;
    final private SystemNotificationService notificationService;

    public ClembleConnectionRepository(String player, ConnectionRepository repository, SystemNotificationService notificationService) {
        this.player = player;
        this.delegate = repository;
        this.notificationService = notificationService;
    }

    @Override
    public String getPlayer() {
        return getPlayer();
    }

    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        return delegate.findAllConnections();
    }

    @Override
    public List<Connection<?>> findConnections(String providerId) {
        return delegate.findConnections(providerId);
    }

    @Override
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        return delegate.findConnections(apiType);
    }

    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUserIds) {
        return delegate.findConnectionsToUsers(providerUserIds);
    }

    @Override
    public Connection<?> getConnection(ConnectionKey connectionKey) {
        return delegate.getConnection(connectionKey);
    }

    @Override
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        return delegate.getConnection(apiType, providerUserId);
    }

    @Override
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        return delegate.getPrimaryConnection(apiType);
    }

    @Override
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        return delegate.findPrimaryConnection(apiType);
    }

    @Override
    public void addConnection(Connection<?> connection) {
        delegate.addConnection(connection);
        // Sending social added, to trigger all related bonuses and staff
        notificationService.send(new SystemPlayerSocialAddedEvent(player, connection.getKey()));
    }

    @Override
    public void updateConnection(Connection<?> connection) {
        delegate.updateConnection(connection);
        // Sending social added, to trigger all related bonuses and staff
        notificationService.send(new SystemPlayerSocialAddedEvent(player, connection.getKey()));
    }

    @Override
    public void removeConnections(String providerId) {
        delegate.removeConnections(providerId);
    }

    @Override
    public void removeConnection(ConnectionKey connectionKey) {
        delegate.removeConnection(connectionKey);
    }
}
