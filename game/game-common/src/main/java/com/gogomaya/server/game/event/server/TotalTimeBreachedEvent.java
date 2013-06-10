package com.gogomaya.server.game.event.server;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.rule.time.TotalTimeRule;

public class TotalTimeBreachedEvent<State extends GameState> extends AbstractTimeBreachEvent<State> {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 6283444607013755399L;

    private TotalTimeRule totalTimeRule;

    public TotalTimeRule getTotalTimeRule() {
        return totalTimeRule;
    }

    public void setTotalTimeRule(TotalTimeRule totalTimeRule) {
        this.totalTimeRule = totalTimeRule;
    }

}
