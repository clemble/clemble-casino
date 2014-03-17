package com.clemble.casino.server.game.action;

import java.util.HashMap;
import java.util.Map;

import com.clemble.casino.game.*;

import com.clemble.casino.game.construct.GameInitiation;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class GameStateFactoryFacade implements ApplicationListener<ContextRefreshedEvent> {

    final private Map<Game, GameStateFactory<?>> gameToStateFactory = new HashMap<Game, GameStateFactory<?>>();

    @SuppressWarnings("unchecked")
    public <S extends RoundGameState> S constructState(final GameInitiation initiation, final RoundGameContext context){
        return (S) gameToStateFactory.get(initiation.getConfiguration().getConfigurationKey().getGame()).constructState(initiation, context);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for(GameStateFactory<?> stateFactory : event.getApplicationContext().getBeansOfType(com.clemble.casino.game.GameStateFactory.class).values())
            gameToStateFactory.put(stateFactory.getGame(), stateFactory);
    }

}
