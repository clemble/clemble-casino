package com.gogomaya.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.event.server.GameServerEvent;

public class GameTimeProcessorListener<State extends GameState> implements GameAspect<State> {

    final GameTimeManagementService<State> timeScheduler;

    public GameTimeProcessorListener(final GameTimeManagementService<State> cacheService) {
        this.timeScheduler = checkNotNull(cacheService);
    }

    @Override
    public void beforeMove(GameSession<State> session, ClientEvent move) {
        timeScheduler.markEnded(session, move.getPlayerId());
    }

    @Override
    public Collection<GameServerEvent<State>> afterMove(GameSession<State> session, Collection<GameServerEvent<State>> events) {
        if(session.getState().complete()) {
            timeScheduler.markFinished(session);
        } else {
            for (ClientEvent nextMove : session.getState().getActionLatch().getActions()) {
                timeScheduler.markStarted(session, nextMove.getPlayerId());
            }
        }
        return events;
    }
}
