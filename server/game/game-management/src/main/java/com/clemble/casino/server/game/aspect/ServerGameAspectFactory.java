package com.clemble.casino.server.game.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameProcessor;
import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.specification.GameConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.OrderComparator;

import java.util.*;

public class ServerGameAspectFactory<GC extends GameConfiguration, C extends GameContext<?>, R extends GameRecord> implements ApplicationListener<ContextRefreshedEvent> {

    final private Logger LOG;

    final private List<GameAspectFactory<?, C, GC>> aspectFactories = new ArrayList<>();
    final private Class<?> aspectFactoryClass;

    public ServerGameAspectFactory(Class<?> aspectFactoryClass) {
        this.aspectFactoryClass = aspectFactoryClass;
        LOG = LoggerFactory.getLogger("GAF - " + aspectFactoryClass.getSimpleName());
    }

    public GameProcessor<R, Event> create(GameProcessor<R, Event> processor, GC configuration, C context) {
        // Step 1. Constructing GameAspects
        Collection<GameAspect<?>> gameAspects = new ArrayList<>(aspectFactories.size());
        for (GameAspectFactory<?, C, GC> aspectFactory : aspectFactories) {
            GameAspect<?> gameAspect = aspectFactory.construct(configuration, context);
            LOG.debug("{} processing aspect factory {} with aspect {}", context.getSession(), aspectFactory, gameAspect);
            if(gameAspect != null) {
                gameAspects.add(aspectFactory.construct(configuration, context));
            }
        }
        return new ServerGameProcessor<>(processor, gameAspects);
    }

    public static class ServerGameProcessor<R extends GameRecord> implements GameProcessor<R, Event> {

        final private static Logger LOG = LoggerFactory.getLogger(ServerGameProcessor.class);

        final private GameAspect<?>[] listenerArray;
        final private GameProcessor<R, Event> processor;

        public ServerGameProcessor(GameProcessor<R, Event> processor, Collection<GameAspect<?>> listeners) {
            this.processor = processor;
            this.listenerArray = listeners.toArray(new GameAspect[0]);
        }

        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public GameManagementEvent process(R record, Event action) {
            LOG.debug("Processing {}", action);
            // Step 1. Before move notification
            for (GameAspect listener : listenerArray)
                listener.onEvent(action);
            // Step 2. Processing in core
            GameManagementEvent event = processor.process(record, action);
            // Step 3 After move notification
            for (GameAspect listener : listenerArray)
                listener.onEvent(event);
            return event;
        }

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for (GameAspectFactory<?, ?, ?> aspectFactory : ((ApplicationContext) event.getSource()).getBeansOfType(GameAspectFactory.class).values())
            check(aspectFactory);
    }

    private Object check(Object bean) {
        //LOG.debug("Processing {}", bean);
        if (aspectFactoryClass.isAssignableFrom(bean.getClass())) {
            // Step 1. Checking that bean is assignable to the basic class
            aspectFactories.add((GameAspectFactory<?, C, GC>) bean);
            LOG.debug("Adding as direct aspect {}", bean);
        } else if (Arrays.asList(bean.getClass().getInterfaces()).contains(GameAspectFactory.class)) {
            // Step 2. Adding general game aspect factories
            aspectFactories.add((GameAspectFactory<?, C, GC>) bean);
            LOG.debug("Adding as generic aspect {}", bean);
        }
        Collections.sort(aspectFactories, OrderComparator.INSTANCE);
        return bean;
    }

}
