package com.clemble.casino.server.game.action;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameProcessor;
import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class ServerGameProcessorFactory<GC extends GameConfiguration, C extends GameContext<?>, R extends GameRecord> implements BeanPostProcessor, ApplicationContextAware {

    final private List<GameAspectFactory<?, C, GC>> aspectFactories = new ArrayList<>();
    final private Class<?> aspectFactoryClass;

    public ServerGameProcessorFactory(Class<?> aspectFactoryClass) {
        this.aspectFactoryClass = aspectFactoryClass;
    }

    public GameProcessor<R, Event> create(GameProcessor<R, Event> processor, GC configuration, C context) {
        Collection<GameAspect<?>> gameAspects = new ArrayList<>(aspectFactories.size());
        for (GameAspectFactory<?, C, GC> aspectFactory : aspectFactories) {
            gameAspects.add(aspectFactory.construct(configuration, context));
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
        @SuppressWarnings({ "rawtypes", "unchecked" })
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
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        for (GameAspectFactory<?, ?, ?> aspectFactory : applicationContext.getBeansOfType(GameAspectFactory.class).values())
            check(aspectFactory);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return check(bean);
    }

    @SuppressWarnings("unchecked")
    private Object check(Object bean) {
        // Step 1. Checking that bean is assignable to the basic class
        if (aspectFactoryClass.isAssignableFrom(bean.getClass())) {
            aspectFactories.add((GameAspectFactory<?, C, GC>) bean);
        }
        // Step 2. Adding general game aspect factories
        if (Arrays.asList(bean.getClass().getInterfaces()).contains(GameAspectFactory.class)) {
            aspectFactories.add((GameAspectFactory<?, C, GC>) bean);
        }
        Collections.sort(aspectFactories, OrderComparator.INSTANCE);
        return bean;
    }

}
