package com.gogomaya.server.game.rule.time.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTimeState;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.event.client.TimeBreachedEvent;
import com.gogomaya.server.game.event.client.TotalTimeBreachedEvent;
import com.gogomaya.server.game.event.server.GameEvent;

public class GameTimeRuleProcessor<State extends GameState> implements GameProcessor<State> {

    final GameTimeRuleJudge<State> timeScheduler;
    final GameProcessor<State> delegate;

    public GameTimeRuleProcessor(final GameTimeRuleJudge<State> cacheService, final GameProcessor<State> delegate) {
        this.delegate = delegate;
        this.timeScheduler = checkNotNull(cacheService);
    }

    @Override
    public Collection<GameEvent<State>> process(GameSession<State> session, ClientEvent move) {
        Collection<GameEvent<State>> events = Collections.emptyList();
        if(move instanceof TimeBreachedEvent) {
            if(move instanceof TotalTimeBreachedEvent) {
                GameTimeState timeState = ((TimeBreachedEvent) move).getTimeState();
                switch (timeState.getTotalTimeRule().getPunishment()) {
                case loose:
                    
                }
            }
        } else {
            // Step 1. Fetching time state
            GameTimeState timeState = timeScheduler.fetch(session);

            timeState.markEnded(move.getPlayerId());
            events = delegate.process(session, move);
            for (ClientEvent nextMove : session.getState().getNextMoves()) {
                timeState.markStarted(nextMove.getPlayerId());
            }
        }
        return events;
    }
}
