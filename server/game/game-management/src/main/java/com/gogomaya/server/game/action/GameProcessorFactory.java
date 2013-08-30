package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameState;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.game.event.server.GameServerEvent;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.aspect.GameAspectFactory;

public class GameProcessorFactory<State extends GameState> {

    final private GameAspectFactory[] aspectFactories;

    @SafeVarargs
    public GameProcessorFactory(final GameAspectFactory... listeners) {
        for (GameAspectFactory listener : listeners)
            checkNotNull(listener);
        this.aspectFactories = Arrays.copyOf(listeners, listeners.length);
    }

    public GameProcessor<State> create(GameInitiation initiation) {
        Collection<GameAspect<State>> gameAspects = new ArrayList<>(aspectFactories.length);
        for (GameAspectFactory aspectFactory : aspectFactories) {
            gameAspects.add(aspectFactory.<State> construct(initiation));
        }
        return new AggregatedGameProcessor<State>(gameAspects);
    }

    public static class AggregatedGameProcessor<State extends GameState> implements GameProcessor<State> {
        final private GameAspect<State>[] listenerArray;

        @SuppressWarnings("unchecked")
        public AggregatedGameProcessor(Collection<GameAspect<State>> listeners) {
            this.listenerArray = new GameAspect[listeners.size()];

            int i = 0;
            for (GameAspect<State> aspect : listeners)
                this.listenerArray[i++] = aspect;
        }

        @Override
        public GameServerEvent<State> process(GameSession<State> session, ClientEvent move) {
            State state = session.getState();
            // Step 1. Before move notification
            for (GameAspect<State> listener : listenerArray) {
                listener.beforeMove(state, move);
            }
            // Step 2. Processing in core
            GameServerEvent<State> event = state.process(move);
            // Step 3. After move notification
            if (session.getState().getOutcome() == null) {
                for (GameAspect<State> listener : listenerArray) {
                    listener.afterMove(session.getState(), event);
                }
            } else {
                // Step 4. After game notification
                for (GameAspect<State> listener : listenerArray) {
                    listener.afterGame(session, event);
                }
            }
            return event;
        }

    }

}
