package com.gogomaya.server.integration.game.tictactoe;

import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.tictactoe.TicTacToe;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.GameSessionPlayerFactory;
import com.gogomaya.server.integration.player.Player;

public class TicTacToePlayerSessionFactory implements GameSessionPlayerFactory<TicTacToeState> {

    /**
     * Generated 04/07/13
     */
    private static final long serialVersionUID = -6361506315379836298L;

    final private GameSessionPlayerFactory<TicTacToeState> sessionPlayerFactory;

    public TicTacToePlayerSessionFactory(GameSessionPlayerFactory<TicTacToeState> gameSessionPlayerFactory) {
        this.sessionPlayerFactory = gameSessionPlayerFactory;
    }

    @Override
    public String getName() {
        return TicTacToe.NAME;
    }

    @Override
    public GameSessionPlayer<TicTacToeState> construct(Player player, GameConstruction construction) {
        return new TicTacToeSessionPlayer(sessionPlayerFactory.construct(player, construction));
    }

    @Override
    public GameSessionPlayer<TicTacToeState> construct(Player player, long construction) {
        return new TicTacToeSessionPlayer(sessionPlayerFactory.construct(player, construction));
    }

}
