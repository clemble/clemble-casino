package com.gogomaya.server.integration.game.construction;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.construct.GameRequest;
import com.gogomaya.server.game.event.schedule.InvitationResponseEvent;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.integration.game.GameSessionPlayerFactory;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.web.active.session.GameConstructionController;
import com.gogomaya.server.web.game.configuration.GameConfigurationManagerController;

public class WebGameConstructionOperations<State extends GameState> extends AbstractGameConstructionOperation<State> {

    /**
     * Generated 03/07/13
     */
    private static final long serialVersionUID = 1433226228732714413L;

    final private GameConfigurationManagerController configuartionManagerController;

    final private GameConstructionController<TicTacToeState> constructionController;

    public WebGameConstructionOperations(final String name, final GameConfigurationManagerController configurationManagerController,
            final GameConstructionController<TicTacToeState> matchController, final GameSessionPlayerFactory<State> playerFactory) {
        super(name, playerFactory);
        this.configuartionManagerController = checkNotNull(configurationManagerController);
        this.constructionController = checkNotNull(matchController);
    }

    @Override
    public GameSpecificationOptions getOptions(String name, Player player) {
        return configuartionManagerController.get(player != null ? player.getPlayerId() : -1L, name);
    }

    @Override
    protected GameConstruction request(Player player, GameRequest request) {
        return constructionController.construct(player.getPlayerId(), request);
    }

    @Override
    protected void response(Player player, InvitationResponseEvent responseEvent) {
        constructionController.invitationResponsed(responseEvent.getPlayerId(), responseEvent);
    }

}
