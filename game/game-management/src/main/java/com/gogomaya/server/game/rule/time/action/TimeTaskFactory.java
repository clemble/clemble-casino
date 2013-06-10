package com.gogomaya.server.game.rule.time.action;

import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameState;

public class TimeTaskFactory<State extends GameState> {

    public TimeTask construct(GameSession<State> session) {
        return new TimeTask(session.getSession(), session.getSpecification().getMoveTimeRule(), session.getSpecification().getTotalTimeRule());
    }

}
