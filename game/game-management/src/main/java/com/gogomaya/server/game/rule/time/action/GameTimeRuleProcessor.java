package com.gogomaya.server.game.rule.time.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.impl.AbstractGameProcessor;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.event.GameEvent;

public class GameTimeRuleProcessor<State extends GameState> extends AbstractGameProcessor<State> {

    final TimeRuleScheduler<State> timeScheduler;

    public GameTimeRuleProcessor(final TimeRuleScheduler<State> cacheService, final GameProcessor<State> delegate) {
        super(delegate);
        this.timeScheduler = checkNotNull(cacheService);
    }

    @Override
    public void beforeMove(GameSession<State> session, GameMove move) {
        TimeTask timeCache = timeScheduler.fetch(session);
        timeCache.markEnded(move.getPlayerId());
    }

    @Override
    public Collection<GameEvent<State>> afterMove(GameSession<State> session, Collection<GameEvent<State>> events) {
        TimeTask timeCache = timeScheduler.fetch(session);
        for (GameMove nextMove : session.getState().getNextMoves()) {
            timeCache.markStarted(nextMove.getPlayerId());
        }
        return events;
    }
}
