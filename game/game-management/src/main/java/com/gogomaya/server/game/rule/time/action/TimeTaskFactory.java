package com.gogomaya.server.game.rule.time.action;

import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTimeState;

public class TimeTaskFactory<State extends GameState> {

    public GameTimeState construct(GameSession<State> session) {
        return new GameTimeState(session.getSession(), session.getSpecification().getMoveTimeRule(), session.getSpecification().getTotalTimeRule());
    }

}
