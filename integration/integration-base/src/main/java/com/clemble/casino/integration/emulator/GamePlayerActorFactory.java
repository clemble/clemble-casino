package com.clemble.casino.integration.emulator;

import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.integration.game.MatchGamePlayer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.*;

/**
 * Created by mavarazy on 23/02/14.
 */
public class GamePlayerActorFactory implements ApplicationContextAware {

    final private Set<GamePlayerActor> actors = new HashSet<>();

    public GamePlayerActor getActor(GameConfiguration configuration) {
        // Step 1. Special case
        if(configuration instanceof PotGameConfiguration)
            return new PotGamePlayerActor(this);
        // Step 2. Processing GamePlayerActor
        for(GamePlayerActor actor: actors)
            if(actor.canPlay(configuration))
                return actor;
        // Step 3. Throwing IllegalArgumentException, if there is no player present
        throw new IllegalArgumentException();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        actors.addAll(applicationContext.getBeansOfType(GamePlayerActor.class).values());
    }

}
