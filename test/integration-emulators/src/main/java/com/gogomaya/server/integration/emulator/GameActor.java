package com.gogomaya.server.integration.emulator;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.integration.game.GamePlayer;

public interface GameActor<State extends GameState> {

    public void move(GamePlayer<State> playerToMove);

}
