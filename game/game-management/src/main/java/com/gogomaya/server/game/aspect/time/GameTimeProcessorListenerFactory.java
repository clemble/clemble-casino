package com.gogomaya.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.game.specification.GameSpecification;

public class GameTimeProcessorListenerFactory<State extends GameState> {

    final private GameTimeManagementService<State> timeManagementService;

    public GameTimeProcessorListenerFactory(GameTimeManagementService<State> timeManagementService) {
        this.timeManagementService = checkNotNull(timeManagementService);
    }

    public GameAspect<State> construct(GameSession<State> session) {
        TimeTracker timeTracker = constructTimeTracker(session);
        if (timeTracker != null) {
            timeManagementService.put(timeTracker);
            return new GameTimeProcessorListener<State>(timeManagementService);
        }
        return null;
    }

    private TimeTracker constructTimeTracker(GameSession<State> session) {
        GameSpecification specification = session.getSpecification();
        TotalTimeRule totalTimeRule = specification.getTotalTimeRule();
        MoveTimeRule moveTimeRule = specification.getMoveTimeRule();
        if (totalTimeRule != null) {
            if (moveTimeRule == null) {
                return new TotalTimeTracker(session.getSession(), totalTimeRule);
            } else {
                return new MoveAndTotalTimeTracker(session.getSession(), totalTimeRule, moveTimeRule);
            }
        } else if (moveTimeRule != null) {
            return new MoveTimeTracker(session.getSession(), moveTimeRule);
        }

        return null;
    }
}
