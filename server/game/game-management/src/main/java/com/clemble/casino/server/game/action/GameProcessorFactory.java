package com.clemble.casino.server.game.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameServerEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.game.aspect.GameManagementAspect;
import com.clemble.casino.server.game.aspect.management.GameSequenceManagementAspect;

public class GameProcessorFactory<State extends GameState> implements BeanPostProcessor {

    private Collection<GameManagementAspect> managementAspects = new HashSet<>();
    private Collection<GameAspectFactory> aspectFactories = new HashSet<>();

    public GameProcessor<State> create(GameInitiation initiation) {
        Collection<GameAspect<State>> gameAspects = new ArrayList<>(aspectFactories.size());
        for (GameAspectFactory aspectFactory : aspectFactories) {
            gameAspects.add(aspectFactory.<State> construct(initiation));
        }
        return new AggregatedGameProcessor<State>(gameAspects, managementAspects);
    }

    public static class AggregatedGameProcessor<State extends GameState> implements GameProcessor<State> {
        final private GameSequenceManagementAspect[] managementListenerArray;
        final private GameAspect<State>[] listenerArray;

        @SuppressWarnings("unchecked")
        public AggregatedGameProcessor(Collection<GameAspect<State>> listeners, Collection<GameManagementAspect> managerListeners) {
            this.listenerArray = listeners.toArray(new GameAspect[0]);
            this.managementListenerArray = managerListeners.toArray(new GameSequenceManagementAspect[0]);
        }

        @Override
        public GameServerEvent<State> process(GameSession<State> session, ClientEvent move) {
            State state = session.getState();
            // Step 1. Before move notification
            for (GameAspect<State> listener : listenerArray) {
                listener.beforeMove(state, move);
            }
            // Step 2. Processing in core
            GameServerEvent<State> event = state.process(session, move);
            if (session.getState().getOutcome() != null) {
                // Step 3.1 After game notification
                for (GameManagementAspect listener : managementListenerArray) {
                    listener.afterGame(session);
                }
            } else {
                // Step 3.2 After move notification
                for (GameAspect<State> listener : listenerArray) {
                    listener.afterMove(session.getState(), event);
                }
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
        if (bean instanceof GameSequenceManagementAspect)
            managementAspects.add((GameSequenceManagementAspect) bean);
        return bean;
    }

}
