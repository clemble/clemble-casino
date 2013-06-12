package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Collection;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.active.time.GameTimeProcessorListenerFactory;
import com.gogomaya.server.game.event.server.GameServerEvent;

public class GameProcessorFactory<State extends GameState> {

    final private GameProcessor<State> coreProcessor;
    final private GameProcessorListener<State>[] registeredListeners;
    private GameTimeProcessorListenerFactory<State> timeListenerFactory;

    @SafeVarargs
    public GameProcessorFactory(final GameProcessor<State> coreProcessor, final GameProcessorListener<State>... listeners) {
        this.coreProcessor = checkNotNull(coreProcessor);
        this.registeredListeners = Arrays.copyOf(listeners, listeners.length);
    }

    public void setTimeListenerFactory(GameTimeProcessorListenerFactory<State> timeListenerFactory) {
        this.timeListenerFactory = timeListenerFactory;
    }

    public GameProcessor<State> create(GameSession<State> session) {
        GameProcessorListener<State> timeListener = timeListenerFactory.construct(session);
        if(timeListener == null) {
            return new AggregatedGameProcessor<State>(coreProcessor, registeredListeners);
        } else {
            GameProcessorListener<State>[] extendedListeners = Arrays.copyOf(registeredListeners, registeredListeners.length + 1);
            extendedListeners[registeredListeners.length] = timeListener;
            return new AggregatedGameProcessor<State>(coreProcessor, extendedListeners);
        }
    }

    public static class AggregatedGameProcessor<State extends GameState> implements GameProcessor<State> {
        final private GameProcessor<State> coreProcessor;
        final private GameProcessorListener<State>[] listeners;

        public AggregatedGameProcessor(GameProcessor<State> coreProcessor, GameProcessorListener<State>[] listeners) {
            this.coreProcessor = coreProcessor;
            this.listeners = listeners;
        }

        @Override
        public Collection<GameServerEvent<State>> process(GameSession<State> session, ClientEvent move) {
            // Step 1. Before move notification
            for (GameProcessorListener<State> listener : listeners) {
                listener.beforeMove(session, move);
            }
            // Step 2. Processing in core
            Collection<GameServerEvent<State>> events = coreProcessor.process(session, move);
            // Step 3. After move notification
            for (GameProcessorListener<State> listener : listeners) {
                listener.afterMove(session, events);
            }
            return events;
        }

    }

}
