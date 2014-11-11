package com.clemble.casino.server.connection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.social.connect.ConnectionKey;

import java.util.Set;

public class MongoPlayerGraph implements PlayerGraph {

    /**
     * Generated 18/12/13
     */
    private static final long serialVersionUID = -1819811612556136513L;

    @Id
    final private String player;
    final private Set<ConnectionKey> owned;
    final private Set<String> connections;

    @JsonCreator
    public MongoPlayerGraph(
        @JsonProperty(PLAYER) String player,
        @JsonProperty("owned") Set<ConnectionKey> owned,
        @JsonProperty("connections") Set<String> connections) {
        this.player = player;
        this.owned = owned;
        this.connections = connections;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public Set<ConnectionKey> getOwned(){
        return owned;
    }

    public void addOwned(ConnectionKey key) {
        this.owned.add(key);
    }

    @Override
    public void addConnection(String player) {
        this.connections.add(player);
    }

    @Override
    public Set<String> fetchConnections() {
        return connections;
    }

    public Set<String> getConnections() {
        return connections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MongoPlayerGraph that = (MongoPlayerGraph) o;

        if (!connections.equals(that.connections)) return false;
        if (!owned.equals(that.owned)) return false;
        if (!player.equals(that.player)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + owned.hashCode();
        result = 31 * result + connections.hashCode();
        return result;
    }
}
