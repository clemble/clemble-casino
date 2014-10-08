package com.clemble.casino.server.game.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.lifecycle.management.GameStateFactory;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.game.lifecycle.initiation.GameInitiation;
import org.reflections.Reflections;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class GameStateFactoryFacade implements ApplicationListener<ContextRefreshedEvent> {

    final private Map<Game, GameStateFactory<?>> gameToStateFactory = new HashMap<Game, GameStateFactory<?>>();

    public GameStateFactoryFacade(){
        Reflections reflections = new Reflections("com.clemble.casino");
        Set<Class<? extends GameStateFactory>> stateFactories = reflections.getSubTypesOf(GameStateFactory.class);
        for(Class<? extends GameStateFactory> factoryClass: stateFactories) {
            try {
                GameStateFactory factory = factoryClass.newInstance();
                gameToStateFactory.put(factory.getGame(), factory);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <S extends RoundGameState> S constructState(final GameInitiation initiation, final RoundGameContext context){
        return (S) gameToStateFactory.get(initiation.getConfiguration().getGame()).constructState(initiation, context);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for(GameStateFactory<?> stateFactory : event.getApplicationContext().getBeansOfType(GameStateFactory.class).values())
            gameToStateFactory.put(stateFactory.getGame(), stateFactory);
    }

}
