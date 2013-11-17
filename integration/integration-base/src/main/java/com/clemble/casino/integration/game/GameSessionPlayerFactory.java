package com.clemble.casino.integration.game;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.integration.player.Player;

public interface GameSessionPlayerFactory {

    public <State extends GameState> GameSessionPlayer<State> construct(Player player, GameConstruction construction);

    public <State extends GameState> GameSessionPlayer<State> construct(Player player, GameSessionKey construction);

}
