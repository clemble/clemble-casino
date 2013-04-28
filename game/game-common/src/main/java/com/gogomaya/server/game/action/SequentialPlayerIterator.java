package com.gogomaya.server.game.action;

import java.util.Collection;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.player.PlayerAware;

public class SequentialPlayerIterator implements GamePlayerIterator {

    /**
     * Generated 12/04/13
     */
    private static final long serialVersionUID = -4182637038671660855L;

    final private long[] players;

    private int index;

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

}
