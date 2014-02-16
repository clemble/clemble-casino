package com.clemble.casino.integration.emulator;

import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.GameState;
import com.clemble.casino.integration.game.MatchGamePlayer;

public interface GameActor<State extends GameState> extends GameAware {

    public void move(MatchGamePlayer<State> playerToMove);

}
