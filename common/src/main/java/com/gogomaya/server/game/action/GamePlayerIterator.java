package com.gogomaya.server.game.action;

import java.util.Collection;

import com.gogomaya.server.player.PlayerAware;

public class GamePlayerIterator {

    final private long[] players;

    private int currentPlayer;

    public GamePlayerIterator(final int currentUser, Collection<? extends PlayerAware> playerAwares) {
        this.currentPlayer = currentUser;
        this.players = new long[playerAwares.size()];

        int i = 0;
        for (PlayerAware playerAware : playerAwares)
            getPlayers()[i++] = playerAware.getPlayerId();
    }

    public long next() {
        return getPlayers()[++currentPlayer % getPlayers().length];
    }

    public long current() {
        return getPlayers()[currentPlayer % getPlayers().length];
    }

    public long[] getPlayers() {
        return players;
    }

}
