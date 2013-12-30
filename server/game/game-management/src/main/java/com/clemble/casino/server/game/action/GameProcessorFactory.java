package com.clemble.casino.server.game.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class GameProcessorFactory<State extends GameState> implements ApplicationContextAware {

    final private Set<GameAspectFactory> aspectFactories = new HashSet<>();

    public GameProcessor<State> create(GameInitiation initiation, GameContext context) {
        Collection<GameAspect<?>> gameAspects = new ArrayList<>(aspectFactories.size());
        for (GameAspectFactory<?> aspectFactory : aspectFactories) {
            gameAspects.add(aspectFactory.construct(initiation, context));
        }
        return new AggregatedGameProcessor<State>(gameAspects);
    }

    public static class AggregatedGameProcessor<State extends GameState> implements GameProcessor<State> {

        final private GameAspect[] listenerArray;

        public AggregatedGameProcessor(Collection<GameAspect<?>> listeners) {
            this.listenerArray = listeners.toArray(new GameAspect[0]);
        }

        @Override
        public GameManagementEvent process(GameSession<State> session, GameAction move) {
            State state = session.getState();
            // Step 1. Before move notification
            for (GameAspect listener : listenerArray) {
                listener.onEvent(move);
            }
            // Step 2. Processing in core
            GameManagementEvent event = state.process(session, move);
            // Step 3 After move notification
            for (GameAspect listener : listenerArray)
                listener.onEvent(event);
            return event;
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        aspectFactories.addAll(applicationContext.getBeansOfType(GameAspectFactory.class).values());
    }

}
