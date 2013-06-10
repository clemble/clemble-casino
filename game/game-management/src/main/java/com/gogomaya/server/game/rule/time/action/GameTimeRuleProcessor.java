package com.gogomaya.server.game.rule.time.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTimeState;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.impl.AbstractGameProcessor;
import com.gogomaya.server.game.event.server.GameEvent;

public class GameTimeRuleProcessor<State extends GameState> extends AbstractGameProcessor<State> {

    final GameTimeRuleJudge<State> timeScheduler;

    public GameTimeRuleProcessor(final GameTimeRuleJudge<State> cacheService, final GameProcessor<State> delegate) {
        super(delegate);
        this.timeScheduler = checkNotNull(cacheService);
    }

    @Override
    public void beforeMove(GameSession<State> session, ClientEvent move) {
        GameTimeState timeCache = timeScheduler.fetch(session);
        timeCache.markEnded(move.getPlayerId());
    }

    @Override
    public Collection<GameEvent<State>> afterMove(GameSession<State> session, Collection<GameEvent<State>> events) {
        GameTimeState timeCache = timeScheduler.fetch(session);
        for (ClientEvent nextMove : session.getState().getNextMoves()) {
            timeCache.markStarted(nextMove.getPlayerId());
        }
        return events;
    }
}
