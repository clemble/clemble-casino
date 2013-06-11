package com.gogomaya.server.game.active.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.action.GameProcessorListener;
import com.gogomaya.server.game.event.server.GameEvent;

public class GameTimeProcessorListener<State extends GameState> implements GameProcessorListener<State> {

    final GameTimeManagementService<State> timeScheduler;

    public GameTimeProcessorListener(final GameTimeManagementService<State> cacheService) {
        this.timeScheduler = checkNotNull(cacheService);
    }

    @Override
    public void beforeMove(GameSession<State> session, ClientEvent move) {
        timeScheduler.markEnded(session, move.getPlayerId());
    }

    @Override
    public Collection<GameEvent<State>> afterMove(GameSession<State> session, Collection<GameEvent<State>> events) {
        if(session.getState().complete()) {
            
        } else {
            for (ClientEvent nextMove : session.getState().getNextMoves()) {
                timeScheduler.markStarted(session, nextMove.getPlayerId());
            }
        }
        return events;
    }
}
