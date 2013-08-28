package com.gogomaya.server.game.iterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.player.PlayerAware;

@JsonTypeName("sequential")
public class SequentialPlayerIterator implements GamePlayerIterator {

    /**
     * Generated 12/04/13
     */
    private static final long serialVersionUID = -4182637038671660855L;

    final private List<Long> players;

    private int index;

    public SequentialPlayerIterator(Collection<Long> playerIds) {
        this.index = 0;
        this.players = new ArrayList<>(playerIds);
    }

    @JsonCreator
    public SequentialPlayerIterator(@JsonProperty("index") final int current, @JsonProperty("players") List<Long> players) {
        this.players = players;
        this.index = current;
    }

    @Override
    public long next() {
        return players.get(++index % getPlayers().size());
    }

    @Override
    public long current() {
        return players.get(index % getPlayers().size());
    }

    @Override
    public Collection<Long> getPlayers() {
        return players;
    }

    @Override
    public Collection<Long> whoIsOpponents(long playerId) {
        Collection<Long> playerIds = new ArrayList<Long>(players);
        playerIds.remove(playerId);
        return playerIds;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean contains(long targetPlayerId) {
        for (long playerId : players) {
            if (playerId == targetPlayerId)
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        result = prime * result + players.hashCode();
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
        SequentialPlayerIterator other = (SequentialPlayerIterator) obj;
        if (index != other.index)
            return false;
        if (!players.equals(other.players))
            return false;
        return true;
    }
    
    public static SequentialPlayerIterator create(Collection<? extends PlayerAware> playerAwares) {
        List<Long> playerIds = new ArrayList<>(playerAwares.size());
        // Parsing player aware values
        for (PlayerAware playerAware : playerAwares) {
            playerIds.add(playerAware.getPlayerId());
        }
        return new SequentialPlayerIterator(playerIds);
    }

}
