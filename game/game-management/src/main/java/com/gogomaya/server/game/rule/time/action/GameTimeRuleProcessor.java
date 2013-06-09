package com.gogomaya.server.game.rule.time.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.server.game.action.GameCacheService;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.impl.AbstractGameProcessor;
import com.gogomaya.server.game.action.impl.GameTimeCache;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.event.GameEvent;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;

public class GameTimeRuleProcessor<State extends GameState> extends AbstractGameProcessor<State> {

    final GameCacheService<State> cacheService;
    final MoveTimeRule moveTimeRule;
    final TotalTimeRule totalTimeRule;

    public GameTimeRuleProcessor(final GameCacheService<State> cacheService, final GameProcessor<State> delegate, final MoveTimeRule moveTimeRule,
            final TotalTimeRule totalTimeRule) {
        super(delegate);
        this.cacheService = checkNotNull(cacheService);
        this.moveTimeRule = moveTimeRule;
        this.totalTimeRule = totalTimeRule;
    }

    @Override
    public void beforeMove(GameSession<State> session, GameMove move) {
        GameTimeCache timeCache = cacheService.get(session).getTimeCache();
        timeCache.markEnded(move.getPlayerId());
        if (moveTimeRule != null) {

        }
        if (totalTimeRule != null) {

        }
    }

    @Override
    public Collection<GameEvent<State>> afterMove(GameSession<State> session, Collection<GameEvent<State>> events) {
        GameTimeCache timeCache = cacheService.get(session).getTimeCache();
        for (GameMove nextMove : session.getState().getNextMoves()) {
            timeCache.markStarted(nextMove.getPlayerId());
        }
        return events;
    }
}
