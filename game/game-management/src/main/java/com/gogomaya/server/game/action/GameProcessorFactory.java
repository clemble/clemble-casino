package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.event.server.GameEvent;
import com.gogomaya.server.game.specification.GameSpecification;

public class GameProcessorFactory<State extends GameState> {

    final private GameProcessor<State> coreProcessor;
    final private LinkedHashSet<GameProcessorListener<State>> listeners;

    @SafeVarargs
    public GameProcessorFactory(final GameProcessor<State> coreProcessor, final GameProcessorListener<State>... listeners) {
        this.coreProcessor = checkNotNull(coreProcessor);
        this.listeners = new LinkedHashSet<GameProcessorListener<State>>(Arrays.asList(listeners));
    }

    public void register(GameProcessorListener<State> listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @SuppressWarnings("unchecked")
    public GameProcessor<State> create(GameSpecification specification) {
        return new AggregatedGameProcessor<State>(coreProcessor, listeners.toArray(new GameProcessorListener[0]));
    }

    public static class AggregatedGameProcessor<State extends GameState> implements GameProcessor<State> {
        final private GameProcessor<State> coreProcessor;
        final private GameProcessorListener<State>[] listeners;

        public AggregatedGameProcessor(GameProcessor<State> coreProcessor, GameProcessorListener<State>[] listeners) {
            this.coreProcessor = coreProcessor;
            this.listeners = listeners;
        }

        @Override
        public Collection<GameEvent<State>> process(GameSession<State> session, ClientEvent move) {
            // Step 1. Before move notification
            for (GameProcessorListener<State> listener : listeners) {
                listener.beforeMove(session, move);
            }
            // Step 2. Processing in core
            Collection<GameEvent<State>> events = coreProcessor.process(session, move);
            // Step 3. After move notification
            for (GameProcessorListener<State> listener : listeners) {
                listener.afterMove(session, events);
            }
            return events;
        }

    }

}
