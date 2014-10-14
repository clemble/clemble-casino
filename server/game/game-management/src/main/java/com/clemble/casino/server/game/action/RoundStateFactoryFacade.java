package com.clemble.casino.server.game.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.lifecycle.management.RoundState;
import com.clemble.casino.game.lifecycle.management.RoundStateFactory;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.game.lifecycle.initiation.GameInitiation;
import org.reflections.Reflections;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class RoundStateFactoryFacade implements ApplicationListener<ContextRefreshedEvent> {

    final private Map<Game, RoundStateFactory<?>> gameToStateFactory = new HashMap<Game, RoundStateFactory<?>>();

    public RoundStateFactoryFacade(){
        Reflections reflections = new Reflections("com.clemble.casino");
        Set<Class<? extends RoundStateFactory>> stateFactories = reflections.getSubTypesOf(RoundStateFactory.class);
        for(Class<? extends RoundStateFactory> factoryClass: stateFactories) {
            try {
                RoundStateFactory factory = factoryClass.newInstance();
                gameToStateFactory.put(factory.getGame(), factory);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <S extends RoundState> S constructState(final GameInitiation initiation, final RoundGameContext context){
        return (S) gameToStateFactory.get(initiation.getConfiguration().getGame()).constructState(initiation, context);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for(RoundStateFactory<?> stateFactory : event.getApplicationContext().getBeansOfType(RoundStateFactory.class).values())
            gameToStateFactory.put(stateFactory.getGame(), stateFactory);
    }

}
