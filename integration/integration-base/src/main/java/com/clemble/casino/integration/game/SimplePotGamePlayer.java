package com.clemble.casino.integration.game;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.integration.game.AbstractGamePlayer;
import com.clemble.casino.integration.game.GamePlayer;
import com.clemble.casino.integration.game.PotGamePlayer;

/**
 * Created by mavarazy on 16/02/14.
 */
public class SimplePotGamePlayer extends AbstractGamePlayer implements PotGamePlayer {

    public SimplePotGamePlayer(ClembleCasinoOperations player, GameConstruction construction) {
        super(player, construction);
    }

    public int getVersion(){
        return 0;
    }

    @Override
    public void giveUp() {

    }

    @Override
    public GamePlayer getСurrent() {
        return null;
    }
}
