package com.clemble.casino.server.player.social;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;
import org.springframework.social.connect.ConnectionKey;

import com.clemble.casino.player.PlayerAware;

@NodeEntity
public class PlayerSocialNetwork implements PlayerAware {

    /**
     * Generated 29/12/13
     */
    private static final long serialVersionUID = 1096381619629303626L;

    @GraphId
    private Long id;

    @Indexed(unique = true, indexName = "playerSocialNetwork", indexType=IndexType.FULLTEXT)
    private String player;

    @RelatedTo(type = "OWN", direction = Direction.OUTGOING)
    private Set<PlayerConnectionKey> owns = new HashSet<PlayerConnectionKey>();

    @RelatedTo(type = "CONNECTED", direction = Direction.OUTGOING)
    private Set<PlayerConnectionKey> connections = new HashSet<PlayerConnectionKey>();

    public PlayerSocialNetwork() {
    }

    public PlayerSocialNetwork(String player) {
        this.player = player;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public PlayerSocialNetwork setPlayer(String player) {
        this.player = player;
        return this;
    }

    public Set<PlayerConnectionKey> getOwns() {
        return owns;
    }

    public PlayerSocialNetwork addOwned(ConnectionKey connectionKey) {
        owns.add(new PlayerConnectionKey(connectionKey));
        return this;
    }
    
    public PlayerSocialNetwork addOwned(Collection<ConnectionKey> connectionKey) {
        for(ConnectionKey connection: connectionKey)
            addOwned(connection);
        return this;
    }

    public void setOwns(Set<PlayerConnectionKey> owns) {
        this.owns = owns;
    }

    public Set<PlayerConnectionKey> getConnections() {
        return connections;
    }

    public PlayerSocialNetwork addConnection(ConnectionKey connectionKey) {
        this.connections.add(new PlayerConnectionKey(connectionKey));
        return this;
    }

    public PlayerSocialNetwork addConnections(Collection<ConnectionKey> connectionKeys) {
        for(ConnectionKey connection: connectionKeys)
            this.connections.add(new PlayerConnectionKey(connection));
        return this;
    }

    public void setConnections(Set<PlayerConnectionKey> connections) {
        this.connections = connections;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((connections == null) ? 0 : connections.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((owns == null) ? 0 : owns.hashCode());
        result = prime * result + ((player == null) ? 0 : player.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerSocialNetwork other = (PlayerSocialNetwork) obj;
        if (connections == null) {
            if (other.connections != null)
                return false;
        } else if (!connections.equals(other.connections))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (owns == null) {
            if (other.owns != null)
                return false;
        } else if (!owns.equals(other.owns))
            return false;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }

}
