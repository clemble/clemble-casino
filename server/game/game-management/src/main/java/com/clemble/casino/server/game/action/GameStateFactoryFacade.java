package com.clemble.casino.server.game.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;

public class GameStateFactoryFacade implements ApplicationContextAware {

    final private Map<Game, GameStateFactory<?>> gameToStateFactory = new HashMap<Game, GameStateFactory<?>>();

    @SuppressWarnings("unchecked")
    public <S extends GameState> S constructState(final GameInitiation initiation, final GameContext context){
        return (S) gameToStateFactory.get(initiation.getConfiguration().getConfigurationKey().getGame()).constructState(initiation, context);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        for(GameStateFactory<?> stateFactory : applicationContext.getBeansOfType(GameStateFactory.class).values())
            gameToStateFactory.put(stateFactory.getGame(), stateFactory);
    }

}
