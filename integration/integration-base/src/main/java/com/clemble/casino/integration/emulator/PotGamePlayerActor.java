package com.clemble.casino.integration.emulator;

import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.integration.game.GamePlayer;
import com.clemble.casino.integration.game.PotGamePlayer;

/**
 * Created by mavarazy on 23/02/14.
 */
public class PotGamePlayerActor implements GamePlayerActor<PotGamePlayer> {

    final GamePlayerActorFactory actorFactory;

    public PotGamePlayerActor(GamePlayerActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    public boolean canPlay(GameConfiguration configuration) {
        return configuration instanceof PotGameConfiguration;
    }

    @Override
    public void play(PotGamePlayer potPlayer) {
        potPlayer.waitForStart();
        while(potPlayer.isAlive()) {
            GamePlayer player = potPlayer.getСurrent();
            GamePlayerActor actor = actorFactory.getActor(player.getConfiguration());
            actor.play(player);
        }
    }

}
