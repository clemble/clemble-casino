package com.clemble.casino.server.connection;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.clemble.casino.player.ConnectionRequest;
import com.clemble.casino.player.PlayerConnections;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;
import org.springframework.social.connect.ConnectionKey;

import com.clemble.casino.player.PlayerAware;
import sun.net.ConnectionResetException;

@NodeEntity
public class GraphPlayerConnections implements PlayerAware {

    /**
     * Generated 29/12/13
     */
    private static final long serialVersionUID = 1096381619629303626L;

    @GraphId
    private Long id;

    @Indexed(unique = true, indexName = "playerSocialNetwork", indexType=IndexType.FULLTEXT)
    private String player;

    @RelatedTo(type = "OWN", direction = Direction.OUTGOING)
    private Set<GraphConnectionKey> owns = new HashSet<GraphConnectionKey>();

    @RelatedTo(type = "CONNECTED", direction = Direction.OUTGOING)
    private Set<GraphConnectionKey> connections = new HashSet<GraphConnectionKey>();

    private Set<ConnectionRequest> connectionRequests = new HashSet<ConnectionRequest>();

    public GraphPlayerConnections() {
    }

    public GraphPlayerConnections(PlayerConnections playerConnections) {
        this.player = playerConnections.getPlayer();

        this.owns = new HashSet<>();
        playerConnections.getOwned().
            forEach(ownedKey -> owns.add(new GraphConnectionKey(ownedKey)));

        this.connections = new HashSet<>();
        playerConnections.getConnected().
            forEach(connectionKey -> this.connections.add(new GraphConnectionKey(connectionKey)));
    }

    public GraphPlayerConnections(String player) {
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

    public GraphPlayerConnections setPlayer(String player) {
        this.player = player;
        return this;
    }

    public Set<GraphConnectionKey> getOwns() {
        return owns;
    }

    public GraphPlayerConnections addOwned(ConnectionKey connectionKey) {
        owns.add(new GraphConnectionKey(connectionKey));
        return this;
    }
    
    public GraphPlayerConnections addOwned(Collection<ConnectionKey> connectionKey) {
        for(ConnectionKey connection: connectionKey)
            addOwned(connection);
        return this;
    }

    public void setOwns(Set<GraphConnectionKey> owns) {
        this.owns = owns;
    }

    public Set<GraphConnectionKey> getConnections() {
        return connections;
    }

    public GraphPlayerConnections addConnection(ConnectionKey connectionKey) {
        this.connections.add(new GraphConnectionKey(connectionKey));
        return this;
    }

    public GraphPlayerConnections addConnections(Collection<ConnectionKey> connectionKeys) {
        for(ConnectionKey connection: connectionKeys)
            this.connections.add(new GraphConnectionKey(connection));
        return this;
    }

    public Set<ConnectionRequest> getConnectionRequests() {
        return connectionRequests;
    }

    public GraphPlayerConnections addConnectionRequest(ConnectionRequest connectionRequest) {
        this.connectionRequests.add(connectionRequest);
        return this;
    }

    public void setConnections(Set<GraphConnectionKey> connections) {
        this.connections = connections;
    }

    public PlayerConnections toPlayerConnections() {
        // Step 1. Converting connection to keys
        Set<ConnectionKey> ownedKeys = new HashSet<>();
        for(GraphConnectionKey connection: owns)
            ownedKeys.add(connection.toConnectionKey());
        // Step 2. Converting connections to keys
        Set<String> connectedKeys = new HashSet<>();
        for(GraphConnectionKey connection: connections)
            connectedKeys.add(connection.getConnectionKey());
        // Step 3. Creating player connections
        return new PlayerConnections(player, ownedKeys, connectedKeys, Collections.emptySet());
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
        GraphPlayerConnections other = (GraphPlayerConnections) obj;
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
