package com.clemble.casino.server.game.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.clemble.casino.game.GameContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.game.aspect.GameManagementAspect;
import com.clemble.casino.server.game.aspect.GameManagementAspecteFactory;

public class GameProcessorFactory<State extends GameState> implements BeanPostProcessor {

    private Collection<GameManagementAspecteFactory> managementAspects = new HashSet<>();
    private Collection<GameAspectFactory> aspectFactories = new HashSet<>();

    public GameProcessor<State> create(GameInitiation initiation, GameContext context) {
        Collection<GameAspect> gameAspects = new ArrayList<>(aspectFactories.size());
        for (GameAspectFactory aspectFactory : aspectFactories) {
            gameAspects.add(aspectFactory.construct(initiation, context));
        }
        Collection<GameManagementAspect> gameManagementAspects = new ArrayList<>(managementAspects.size());
        for(GameManagementAspecteFactory managementAspecteFactory: managementAspects)
            gameManagementAspects.add(managementAspecteFactory.construct(initiation));
        return new AggregatedGameProcessor<State>(gameAspects, gameManagementAspects);
    }

    public static class AggregatedGameProcessor<State extends GameState> implements GameProcessor<State> {
        final private GameManagementAspect[] managementListenerArray;
        final private GameAspect[] listenerArray;

        @SuppressWarnings("unchecked")
        public AggregatedGameProcessor(Collection<GameAspect> listeners, Collection<GameManagementAspect> managerListeners) {
            this.listenerArray = listeners.toArray(new GameAspect[0]);
            this.managementListenerArray = managerListeners.toArray(new GameManagementAspect[0]);
        }

        @Override
        public GameManagementEvent<State> process(GameSession<State> session, GameAction move) {
            State state = session.getState();
            // Step 1. Before move notification
            for (GameAspect listener : listenerArray) {
                listener.onEvent(move);
            }
            // Step 2. Processing in core
            GameManagementEvent<State> event = state.process(session, move);
            if (session.getState().getOutcome() != null) {
                // Step 3.1 After game notification
                for (GameManagementAspect listener : managementListenerArray) {
                    listener.afterGame(session);
                }
            }
            // Step 3.2 After move notification
            for (GameAspect listener : listenerArray) {
                listener.onEvent(event);
            }
            return event;
        }

    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof GameAspectFactory)
            aspectFactories.add((GameAspectFactory) bean);
        if (bean instanceof GameManagementAspecteFactory)
            managementAspects.add((GameManagementAspecteFactory) bean);
        return bean;
    }

}
