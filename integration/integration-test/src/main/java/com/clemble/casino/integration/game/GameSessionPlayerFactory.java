package com.clemble.casino.integration.game;

import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.integration.game.GameSessionPlayer;
import com.clemble.casino.integration.player.Player;

public interface GameSessionPlayerFactory<State extends GameState> extends GameAware {

    public GameSessionPlayer<State> construct(Player player, GameConstruction construction);

    public GameSessionPlayer<State> construct(Player player, GameSessionKey construction);

}
