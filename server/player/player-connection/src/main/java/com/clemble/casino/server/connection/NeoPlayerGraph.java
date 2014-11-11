package com.clemble.casino.server.connection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.clemble.casino.WebMapping;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;
import org.springframework.social.connect.ConnectionKey;

import com.clemble.casino.player.PlayerAware;

@NodeEntity
public class NeoPlayerGraph implements PlayerGraph {

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

    public NeoPlayerGraph() {
    }

    public NeoPlayerGraph(String player) {
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

    public NeoPlayerGraph setPlayer(String player) {
        this.player = player;
        return this;
    }

    public Set<GraphConnectionKey> getOwns() {
        return owns;
    }

    public void setOwns(Set<GraphConnectionKey> owns) {
        this.owns = owns;
    }

    public Set<GraphConnectionKey> getConnections() {
        return connections;
    }

    public void setConnections(Set<GraphConnectionKey> connections) {
        this.connections = connections;
    }

    public void addOwned(ConnectionKey connectionKey) {
        owns.add(new GraphConnectionKey(connectionKey));
    }

    public void addConnection(String player) {
        this.connections.add(new GraphConnectionKey(new ConnectionKey(WebMapping.PROVIDER_ID, player)));
    }

    @Override
    public Set<String> fetchConnections() {
        return connections.
            stream().
            map(c -> c.toConnectionKey().getProviderUserId()).
            collect(Collectors.toSet());
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
        NeoPlayerGraph other = (NeoPlayerGraph) obj;
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
