package com.gogomaya.server.game;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.player.PlayerAware;

@JsonTypeName("sequential")
public class SequentialPlayerIterator implements GamePlayerIterator {

    /**
     * Generated 12/04/13
     */
    private static final long serialVersionUID = -4182637038671660855L;

    final private long[] players;

    private int index;

    public SequentialPlayerIterator(Collection<? extends PlayerAware> playerAwares) {
        this.index = 0;
        this.players = new long[playerAwares.size()];
        // Parsing player aware values
        for (PlayerAware playerAware : playerAwares) {
            players[index++] = playerAware.getPlayerId();
        }
    }

    @JsonCreator
    public SequentialPlayerIterator(@JsonProperty("index") final int current, @JsonProperty("players") long[] players) {
        this.players = players;
        this.index = current;
    }

    public SequentialPlayerIterator(final int currentUser, Collection<? extends PlayerAware> playerAwares) {
        this.index = currentUser;
        this.players = new long[playerAwares.size()];

        int i = 0;
        for (PlayerAware playerAware : playerAwares)
            getPlayers()[i++] = playerAware.getPlayerId();
    }

    @Override
    public long next() {
        return getPlayers()[++index % getPlayers().length];
    }

    @Override
    public long current() {
        return getPlayers()[index % getPlayers().length];
    }

    @Override
    public long[] getPlayers() {
        return players;
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
        result = prime * result + Arrays.hashCode(players);
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
        if (!Arrays.equals(players, other.players))
            return false;
        return true;
    }

}
