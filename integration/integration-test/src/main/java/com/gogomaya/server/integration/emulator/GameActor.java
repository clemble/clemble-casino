package com.gogomaya.server.integration.emulator;

import com.gogomaya.server.game.GameAware;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.integration.game.GameSessionPlayer;

public interface GameActor<State extends GameState> extends GameAware {

    public void move(GameSessionPlayer<State> playerToMove);

}
