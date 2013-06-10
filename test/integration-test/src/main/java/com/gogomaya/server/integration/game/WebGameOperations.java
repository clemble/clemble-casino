package com.gogomaya.server.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.tictactoe.TicTacToeState;
import com.gogomaya.server.web.active.session.GameConstructionController;
import com.gogomaya.server.web.game.configuration.GameConfigurationManagerController;

public class WebGameOperations<State extends GameState> extends AbstractGameOperation<State> {

    final private GameConfigurationManagerController configuartionManagerController;

    final private GameConstructionController<TicTacToeState> matchController;

    public WebGameOperations(final GameConfigurationManagerController configurationManagerController,
            final GameConstructionController<TicTacToeState> matchController,
            final GamePlayerFactory<State> playerFactory,
            final PlayerOperations playerOperations) {
        super(playerOperations, playerFactory);
        this.configuartionManagerController = checkNotNull(configurationManagerController);
        this.matchController = checkNotNull(matchController);
    }

    @Override
    public GameSpecificationOptions getOptions() {
        return getOptions(null);
    }

    @Override
    public GameSpecificationOptions getOptions(Player player) {
        return configuartionManagerController.get(player != null ? player.getPlayerId() : -1L);
    }

    @Override
    @SuppressWarnings("unchecked")
    public GameTable<State> start(Player player, GameSpecification gameSpecification) {
        return (GameTable<State>) matchController.match(player.getPlayerId(), gameSpecification);
    }

}
