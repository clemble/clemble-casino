package com.clemble.casino.integration.emulator;

import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.integration.game.GamePlayer;
import com.clemble.casino.integration.game.MatchGamePlayer;

/**
 * Created by mavarazy on 23/02/14.
 */
public interface GamePlayerActor<T extends GamePlayer> {

    public boolean canPlay(GameConfiguration configuration);

    public void play(T playerToMove);

}
