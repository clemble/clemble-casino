package com.clemble.casino.server.game.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.action.management.GameManagementAction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class GameProcessorFactory<State extends GameState> implements BeanPostProcessor, ApplicationContextAware {

    final private List<GameAspectFactory> aspectFactories = new ArrayList<>();

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
        Collections.sort(aspectFactories, OrderComparator.INSTANCE);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof GameAspectFactory) {
            aspectFactories.add((GameAspectFactory) bean);
            Collections.sort(aspectFactories, OrderComparator.INSTANCE);
        }
        return bean;
    }

}
