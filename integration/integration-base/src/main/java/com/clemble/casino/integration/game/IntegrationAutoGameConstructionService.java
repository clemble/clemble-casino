package com.clemble.casino.integration.game;

import com.clemble.casino.game.construction.AutomaticGameRequest;
import com.clemble.casino.game.construction.GameConstruction;
import com.clemble.casino.game.construction.service.AutoGameConstructionService;
import com.clemble.casino.server.game.construction.controller.AutoGameConstructionController;

import java.util.Collection;

/**
 * Created by mavarazy on 9/15/14.
 */
public class IntegrationAutoGameConstructionService implements AutoGameConstructionService {

    final private String player;
    final private AutoGameConstructionController constructionService;

    public IntegrationAutoGameConstructionService(String player, AutoGameConstructionController constructionService) {
        this.player = player;
        this.constructionService = constructionService;
    }

    @Override
    public GameConstruction construct(AutomaticGameRequest request) {
        return constructionService.construct(player, request);
    }

    @Override
    public Collection<GameConstruction> getPending(String player) {
        return constructionService.getPending(player);
    }

}
