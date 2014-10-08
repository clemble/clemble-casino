package com.clemble.casino.server.game.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.server.game.action.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.OrderComparator;

import java.util.*;

public class ServerGameManagerFactory<GC extends GameConfiguration, C extends GameContext<?>> implements ApplicationListener<ContextRefreshedEvent> {

    final private Logger LOG;

    final private List<GameAspectFactory<?, C, GC>> aspectFactories = new ArrayList<>();
    final private Class<?> aspectFactoryClass;

    public ServerGameManagerFactory(Class<?> aspectFactoryClass) {
        this.aspectFactoryClass = aspectFactoryClass;
        LOG = LoggerFactory.getLogger("GAF - " + aspectFactoryClass.getSimpleName());
    }

    public GameManager<C> create(GameState<C, ? extends Event> processor, GC configuration, C context) {
        // Step 1. Constructing GameAspects
        Collection<GameAspect<?>> gameAspects = new ArrayList<>(aspectFactories.size());
        for (GameAspectFactory<?, C, GC> aspectFactory : aspectFactories) {
            GameAspect<?> gameAspect = aspectFactory.construct(configuration, context);
            LOG.debug("{} processing aspect factory {} with aspect {}", context.getSessionKey(), aspectFactory, gameAspect);
            if(gameAspect != null) {
                gameAspects.add(aspectFactory.construct(configuration, context));
            }
        }
        return new GameManager<>(context, processor, gameAspects);
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
