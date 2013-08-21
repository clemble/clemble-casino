package com.gogomaya.server.integration.game.tictactoe;

import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.GameSessionPlayerFactory;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.tictactoe.PicPacPoeState;

public class PicPacPoePlayerSessionFactory implements GameSessionPlayerFactory<PicPacPoeState> {

    /**
     * Generated 04/07/13
     */
    private static final long serialVersionUID = -6361506315379836298L;

    final private GameSessionPlayerFactory<PicPacPoeState> sessionPlayerFactory;

    public PicPacPoePlayerSessionFactory(GameSessionPlayerFactory<PicPacPoeState> gameSessionPlayerFactory) {
        this.sessionPlayerFactory = gameSessionPlayerFactory;
    }

    @Override
    public Game getGame() {
        return Game.pic;
    }

    @Override
    public GameSessionPlayer<PicPacPoeState> construct(Player player, GameConstruction construction) {
        return new PicPacPoeSessionPlayer(sessionPlayerFactory.construct(player, construction));
    }

    @Override
    public GameSessionPlayer<PicPacPoeState> construct(Player player, long construction) {
        return new PicPacPoeSessionPlayer(sessionPlayerFactory.construct(player, construction));
    }

}
