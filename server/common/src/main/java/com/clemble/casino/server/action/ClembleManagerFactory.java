package com.clemble.casino.server.action;

import com.clemble.casino.event.Event;
import com.clemble.casino.lifecycle.configuration.Configuration;
import com.clemble.casino.lifecycle.management.State;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.aspect.ClembleAspectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.OrderComparator;

import java.util.*;

/**
 * Created by mavarazy on 10/9/14.
 */
public class ClembleManagerFactory<C extends Configuration> implements ApplicationListener<ContextRefreshedEvent> {

    final private Logger LOG;

    final private List<ClembleAspectFactory<?, C, State>> aspectFactories = new ArrayList<>();
    final private Class<?>[] aspectFactoryClasses;

    public ClembleManagerFactory(Class<?>... aspectFactoryClass) {
        this.aspectFactoryClasses = aspectFactoryClass;
        LOG = LoggerFactory.getLogger("CMF - " + aspectFactoryClass[0].getSimpleName());
    }

    public <R extends Event, S extends State<R, ?>> ClembleManager<R, S> create(S state, C configuration) {
        // Step 1. Constructing GameAspects
        Collection<ClembleAspect<?>> gameAspects = new ArrayList<>(aspectFactories.size());
        for (ClembleAspectFactory<?, C, State> aspectFactory : aspectFactories) {
            ClembleAspect<?> gameAspect = aspectFactory.construct(configuration, (State) state);
            LOG.debug("processing aspect factory {} with aspect {}", aspectFactory, gameAspect);
            if(gameAspect != null) {
                gameAspects.add(gameAspect);
            }
        }
        return new ClembleManager<R, S>(state, gameAspects);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for (ClembleAspectFactory<?, C, ?> aspectFactory : ((ApplicationContext) event.getSource()).getBeansOfType(ClembleAspectFactory.class).values())
            check(aspectFactory);
    }

    private Object check(Object bean) {
        //LOG.debug("Processing {}", bean);
        for (Class<?> aspectFactoryClass: aspectFactoryClasses) {
            if (aspectFactoryClass.isAssignableFrom(bean.getClass())) {
                // Step 1. Checking that bean is assignable to the basic class
                aspectFactories.add((ClembleAspectFactory<?, C, State>) bean);
                LOG.debug("Adding aspect {}", bean);
            }
        }
        Collections.sort(aspectFactories, OrderComparator.INSTANCE);
        return bean;
    }

}
