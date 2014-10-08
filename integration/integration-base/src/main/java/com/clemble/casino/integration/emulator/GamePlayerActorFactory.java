package com.clemble.casino.integration.emulator;

import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.*;

/**
 * Created by mavarazy on 23/02/14.
 */
public class GamePlayerActorFactory implements ApplicationListener<ContextRefreshedEvent> {

    final private Set<GamePlayerActor> actors = new HashSet<>();

    public GamePlayerActor getActor(GameConfiguration configuration) {
        // Step 1. Special case
        if(configuration instanceof MatchGameConfiguration)
            return new MatchGamePlayerActor(this);
        // Step 2. Processing GamePlayerActor
        for(GamePlayerActor actor: actors)
            if(actor.canPlay(configuration))
                return actor;
        // Step 3. Throwing IllegalArgumentException, if there is no player present
        throw new IllegalArgumentException();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        actors.addAll(event.getApplicationContext().getBeansOfType(GamePlayerActor.class).values());
    }

}
