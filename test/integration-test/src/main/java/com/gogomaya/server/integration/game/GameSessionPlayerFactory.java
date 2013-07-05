package com.gogomaya.server.integration.game;

import com.gogomaya.server.game.GameAware;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.integration.player.Player;

public interface GameSessionPlayerFactory<State extends GameState> extends GameAware {

    public GameSessionPlayer<State> construct(Player player, GameConstruction construction);

    public GameSessionPlayer<State> construct(Player player, long construction);

}
