package com.clemble.casino.server.game.pending;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.clemble.casino.player.PlayerAware;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class PendingPlayer implements PlayerAware {

    /**
     * Generated 06/01/14
     */
    private static final long serialVersionUID = -8995526667701103328L;

    @GraphId
    private Long id;

    @Indexed(unique = true, indexName = "pendingPlayerIdx", indexType = IndexType.FULLTEXT)
    private String player;

    public PendingPlayer() {
    }

    public PendingPlayer(String player) {
        this.player = player;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long playerId) {
        this.id = playerId;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    @Override
    public String toString(){
        return player;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        PendingPlayer other = (PendingPlayer) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }

}
