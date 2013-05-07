package com.gogomaya.server.game.action;

import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.event.GameEvent;

public interface GameStateProcessor<State extends GameState> {

    public GameEvent<State> process(final Long sessionId, final GameMove gameMove);

}
