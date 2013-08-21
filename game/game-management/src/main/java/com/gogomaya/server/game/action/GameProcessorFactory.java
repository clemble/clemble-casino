package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.aspect.GameAspectFactory;
import com.gogomaya.server.game.construct.GameInitiation;
import com.gogomaya.server.game.event.server.GameServerEvent;

public class GameProcessorFactory<State extends GameState> {

    final private GameProcessor<State> coreProcessor;
    final private GameAspectFactory[] aspectFactories;

    @SafeVarargs
    public GameProcessorFactory(final GameProcessor<State> coreProcessor, final GameAspectFactory... listeners) {
        this.coreProcessor = checkNotNull(coreProcessor);
        for (GameAspectFactory listener : listeners)
            checkNotNull(listener);
        this.aspectFactories = Arrays.copyOf(listeners, listeners.length);
    }

    public GameProcessor<State> create(GameInitiation initiation) {
        Collection<GameAspect<State>> gameAspects = new ArrayList<>(aspectFactories.length);
        for (GameAspectFactory aspectFactory : aspectFactories) {
            gameAspects.add(aspectFactory.<State> construct(initiation));
        }
        return new AggregatedGameProcessor<State>(coreProcessor, gameAspects);
    }

    public static class AggregatedGameProcessor<State extends GameState> implements GameProcessor<State> {
        final private GameProcessor<State> coreProcessor;
        final private GameAspect<State>[] listenerArray;

        @SuppressWarnings("unchecked")
        public AggregatedGameProcessor(GameProcessor<State> coreProcessor, Collection<GameAspect<State>> listeners) {
            this.coreProcessor = coreProcessor;
            this.listenerArray = new GameAspect[listeners.size()];

            int i = 0;
            for (GameAspect<State> aspect : listeners)
                this.listenerArray[i++] = aspect;
        }

        @Override
        public GameServerEvent<State> process(GameSession<State> session, ClientEvent move) {
            // Step 1. Before move notification
            for (GameAspect<State> listener : listenerArray) {
                listener.beforeMove(session.getState(), move);
            }
            // Step 2. Processing in core
            GameServerEvent<State> events = coreProcessor.process(session, move);
            // Step 3. After move notification
            if (session.getState().getOutcome() == null) {
                for (GameAspect<State> listener : listenerArray) {
                    listener.afterMove(session.getState(), events);
                }
            } else {
                // Step 4. After game notification
                for (GameAspect<State> listener : listenerArray) {
                    listener.afterGame(session, events);
                }
            }
            return events;
        }

    }

}
