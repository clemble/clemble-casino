package com.clemble.casino.server.game.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;

import com.clemble.casino.game.GameState;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.MatchGameRecord;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;

public class MatchGameProcessorFactory<S extends GameState> implements BeanPostProcessor, ApplicationContextAware {

    @SuppressWarnings("rawtypes")
    final private List<MatchGameAspectFactory> aspectFactories = new ArrayList<>();

    public MatchGameProcessor<S> create(MatchGameConfiguration configuration, MatchGameContext context) {
        Collection<GameAspect<?>> gameAspects = new ArrayList<>(aspectFactories.size());
        for (MatchGameAspectFactory<?> aspectFactory : aspectFactories) {
            gameAspects.add(aspectFactory.construct(configuration, context));
        }
        return new AggregatedGameProcessor<>(gameAspects);
    }

    public static class AggregatedGameProcessor<S extends GameState> implements MatchGameProcessor<S> {

        final private static Logger LOG = LoggerFactory.getLogger(AggregatedGameProcessor.class);

        final private GameAspect<?>[] listenerArray;

        public AggregatedGameProcessor(Collection<GameAspect<?>> listeners) {
            this.listenerArray = listeners.toArray(new GameAspect[0]);
        }

        @Override
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public GameManagementEvent process(MatchGameRecord<S> session, GameAction move) {
            LOG.debug("Processing {}", move);
            GameState state = session.getState();
            // Step 1. Before move notification
            for (GameAspect listener : listenerArray)
                listener.onEvent(move);
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
        aspectFactories.addAll(applicationContext.getBeansOfType(MatchGameAspectFactory.class).values());
        Collections.sort(aspectFactories, OrderComparator.INSTANCE);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MatchGameAspectFactory) {
            aspectFactories.add((MatchGameAspectFactory) bean);
            Collections.sort(aspectFactories, OrderComparator.INSTANCE);
        }
        return bean;
    }

}
