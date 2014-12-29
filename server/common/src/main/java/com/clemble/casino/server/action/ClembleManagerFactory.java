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
        Collection<ClembleAspect<?>> aspects = new ArrayList<>(aspectFactories.size());
        for (ClembleAspectFactory<?, C, State> aspectFactory : aspectFactories) {
            ClembleAspect<?> aspect = aspectFactory.construct(configuration, (State) state);
            LOG.debug("processing aspect factory {} with aspect {}", aspectFactory, aspect);
            if(aspect != null) {
                aspects.add(aspect);
            }
        }
        return new ClembleManager<R, S>(state, aspects);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for (ClembleAspectFactory<?, C, ?> aspectFactory : ((ApplicationContext) event.getSource()).getBeansOfType(ClembleAspectFactory.class).values())
            check(aspectFactory);
    }

    private Object check(Object bean) {
        // Step 1. Checking bean is applicable
        for (Class<?> aspectFactoryClass: aspectFactoryClasses) {
            if (aspectFactoryClass.isAssignableFrom(bean.getClass())) {
                ClembleAspectFactory<?, C, State> aspectFactory = (ClembleAspectFactory<?, C, State>) bean;
                // Step 1. Checking that bean is assignable to the basic class
                aspectFactories.add(aspectFactory);
                LOG.debug("Adding aspect {} {}", aspectFactory.getOrder(), aspectFactory);
                Collections.sort(aspectFactories, OrderComparator.INSTANCE);
                checkOrder();
            }
        }
        // Step 3. Returning processed bean
        return bean;
    }

    private void checkOrder() {
        HashSet<Integer> aspectIdentifiers = new HashSet<>();
        for(ClembleAspectFactory aspectFactory: aspectFactories) {
            aspectIdentifiers.add(aspectFactory.getOrder());
        }
        if(aspectIdentifiers.size() != aspectFactories.size()) {
            for(ClembleAspectFactory aspectFactory: aspectFactories)
                LOG.error("{} - {}", aspectFactory.getOrder(), aspectFactory);
            throw new IllegalArgumentException("Aspect factory order overlaps, which should never happen");
        }
    }

}
