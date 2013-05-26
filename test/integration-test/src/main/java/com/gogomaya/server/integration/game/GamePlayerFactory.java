package com.gogomaya.server.integration.game;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.integration.player.Player;

public interface GamePlayerFactory<State extends GameState> {

    public GamePlayer<State> construct(Player player, GameTable<State> table);

}
