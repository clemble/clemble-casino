package com.clemble.casino.integration.emulator;

import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.integration.game.GamePlayer;
import com.clemble.casino.integration.game.MatchGamePlayer;

/**
 * Created by mavarazy on 23/02/14.
 */
public class MatchGamePlayerActor implements GamePlayerActor<MatchGamePlayer> {

    final GamePlayerActorFactory actorFactory;

    public MatchGamePlayerActor(GamePlayerActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    public boolean canPlay(GameConfiguration configuration) {
        return configuration instanceof MatchGameConfiguration;
    }

    @Override
    public void play(MatchGamePlayer potPlayer) {
        potPlayer.waitForStart();
        while(potPlayer.isAlive()) {
            GamePlayer player = potPlayer.getСurrent();
            GamePlayerActor actor = actorFactory.getActor(player.getConfiguration());
            actor.play(player);
        }
    }

}
