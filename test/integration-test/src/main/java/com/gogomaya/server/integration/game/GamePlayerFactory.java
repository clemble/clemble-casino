package com.gogomaya.server.integration.game;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.integration.player.Player;

public interface GamePlayerFactory<State extends GameState> {

    public GamePlayer<State> construct(Player player, GameTable<State> table);

}
