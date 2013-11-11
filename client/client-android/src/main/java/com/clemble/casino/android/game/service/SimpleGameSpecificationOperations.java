package com.clemble.casino.android.game.service;

import com.clemble.casino.client.game.GameSpecificationOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.configuration.GameSpecificationOptions;
import com.clemble.casino.game.service.GameSpecificationService;

public class SimpleGameSpecificationOperations implements GameSpecificationOperations {

    final private String player;
    final private GameSpecificationService specificationService;

    public SimpleGameSpecificationOperations(String player, GameSpecificationService specificationService) {
        this.player = player;
        this.specificationService = specificationService;
    }

    @Override
    public GameSpecificationOptions get(Game game) {
        return specificationService.getSpecificationOptions(player, game);
    }

}
