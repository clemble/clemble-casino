package com.gogomaya.server.game.tictactoe;

import java.util.Collection;

import com.gogomaya.server.game.action.GamePlayerIterator;
import com.gogomaya.server.player.PlayerAware;

public class TicTacToePlayerIterator implements GamePlayerIterator {

    /**
     * Generated 12/04/13
     */
    private static final long serialVersionUID = -4182637038671660855L;

    final private long[] players;

    private int currentPlayer;

    public TicTacToePlayerIterator(final int currentUser, Collection<? extends PlayerAware> playerAwares) {
        this.currentPlayer = currentUser;
        this.players = new long[playerAwares.size()];

        int i = 0;
        for (PlayerAware playerAware : playerAwares)
            getPlayers()[i++] = playerAware.getPlayerId();
    }

    @Override
    public long next() {
        return getPlayers()[++currentPlayer % getPlayers().length];
    }

    @Override
    public long current() {
        return getPlayers()[currentPlayer % getPlayers().length];
    }

    @Override
    public long[] getPlayers() {
        return players;
    }

}
