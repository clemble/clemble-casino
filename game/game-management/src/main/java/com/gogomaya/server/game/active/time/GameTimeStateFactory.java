package com.gogomaya.server.game.active.time;

import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;

public class GameTimeStateFactory<State extends GameState> {

    public GameTimeState construct(GameSession<State> session) {
        return new GameTimeState(session.getSession(), session.getSpecification().getMoveTimeRule(), session.getSpecification().getTotalTimeRule());
    }

}
