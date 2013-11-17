package com.clemble.casino.integration.game;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.integration.player.Player;
import com.clemble.casino.server.web.game.session.GameActionController;
import com.clemble.casino.server.web.game.session.GameConstructionController;

public class WebGameSessionPlayerFactory implements GameSessionPlayerFactory {

    final private GameActionController<?> engineController;
    final private GameConstructionController<?> constructionController;

    public WebGameSessionPlayerFactory(GameActionController<?> engineController, GameConstructionController<?> constructionController) {
        this.engineController = engineController;
        this.constructionController = constructionController;
    }

    @Override
    public <State extends GameState> GameSessionPlayer<State> construct(Player player, GameConstruction construction) {
        return (GameSessionPlayer<State>) new WebGameSessionPlayer(player, construction, engineController);
    }

    @Override
    public <State extends GameState> GameSessionPlayer<State> construct(Player player, GameSessionKey construction) {
        return construct(player, constructionController.getConstruct(player.getPlayer(), construction.getGame(), construction.getSession()));
    }

}
