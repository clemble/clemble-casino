package com.gogomaya.server.integration.game;

import com.gogomaya.game.Game;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.GameState;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.web.game.session.GameActionController;
import com.gogomaya.server.web.game.session.GameConstructionController;

public class WebGameSessionPlayerFactory<State extends GameState> implements GameSessionPlayerFactory<State> {

    /**
     * Generated 04/07/13
     */
    private static final long serialVersionUID = -7652085755416835994L;

    final private GameActionController<State> engineController;
    final private GameConstructionController<State> constructionController;

    public WebGameSessionPlayerFactory(GameActionController<State> engineController, GameConstructionController<State> constructionController) {
        this.engineController = engineController;
        this.constructionController = constructionController;
    }

    @Override
    public Game getGame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GameSessionPlayer<State> construct(Player player, GameConstruction construction) {
        return new WebGameSessionPlayer<>(player, construction, engineController);
    }

    @Override
    public GameSessionPlayer<State> construct(Player player, GameSessionKey construction) {
        return new WebGameSessionPlayer<>(player, constructionController.getConstruct(player.getPlayerId(), construction.getSession()), engineController);
    }

}
