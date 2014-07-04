package com.clemble.casino.server.event;

import com.clemble.casino.player.ConnectionKeyAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.social.connect.ConnectionKey;

import java.util.Collection;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemPlayerConnectionsFetchedEvent implements SystemEvent, ConnectionKeyAware {

    final public static String CHANNEL = "player:social:connections:fetched";

    final private String player;
    final private ConnectionKey connectionKey;
    final private Collection<ConnectionKey> connections;

    @JsonCreator
    public SystemPlayerConnectionsFetchedEvent(@JsonProperty("player") String player, @JsonProperty("connectionKey") ConnectionKey connectionKey, @JsonProperty("connections") Collection<ConnectionKey> connections) {
        this.player = player;
        this.connectionKey = connectionKey;
        this.connections = connections;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public ConnectionKey getConnection() {
        return connectionKey;
    }

    public Collection<ConnectionKey> getConnections() {
        return connections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemPlayerConnectionsFetchedEvent that = (SystemPlayerConnectionsFetchedEvent) o;

        if (connectionKey != null ? !connectionKey.equals(that.connectionKey) : that.connectionKey != null)
            return false;
        if (connections != null ? !connections.equals(that.connections) : that.connections != null) return false;
        if (player != null ? !player.equals(that.player) : that.player != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player != null ? player.hashCode() : 0;
        result = 31 * result + (connectionKey != null ? connectionKey.hashCode() : 0);
        result = 31 * result + (connections != null ? connections.hashCode() : 0);
        return result;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
