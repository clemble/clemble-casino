package com.gogomaya.server.integration.game.construction;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.Game;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.GameState;
import com.gogomaya.game.configuration.GameSpecificationOptions;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameRequest;
import com.gogomaya.game.event.schedule.InvitationResponseEvent;
import com.gogomaya.server.integration.game.GameSessionPlayerFactory;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.web.game.options.GameConfigurationManagerController;
import com.gogomaya.server.web.game.session.GameConstructionController;

public class WebGameConstructionOperations<State extends GameState> extends AbstractGameConstructionOperation<State> {

    /**
     * Generated 03/07/13
     */
    private static final long serialVersionUID = 1433226228732714413L;

    final private GameConfigurationManagerController configuartionManagerController;

    final private GameConstructionController<State> constructionController;

    public WebGameConstructionOperations(final Game game,
            final GameConfigurationManagerController configurationManagerController,
            final GameConstructionController<State> matchController,
            final GameSessionPlayerFactory<State> playerFactory) {
        super(game, playerFactory);
        this.configuartionManagerController = checkNotNull(configurationManagerController);
        this.constructionController = checkNotNull(matchController);
    }

    @Override
    public GameSpecificationOptions getOptions(Game game, Player player) {
        return configuartionManagerController.getSpecificationOptions(player != null ? player.getPlayer() : null, game);
    }

    @Override
    protected GameConstruction request(Player player, GameRequest request) {
        return constructionController.construct(player.getPlayer(), request);
    }

    @Override
    protected void response(Player player, InvitationResponseEvent responseEvent) {
        constructionController.reply(player.getPlayer(), responseEvent.getSession().getSession(), responseEvent);
    }

    @Override
    public ClientEvent constructionResponse(Player player, String requested, GameSessionKey construction) {
        return constructionController.getResponce(player.getPlayer(), construction.getSession(), requested);
    }

}
