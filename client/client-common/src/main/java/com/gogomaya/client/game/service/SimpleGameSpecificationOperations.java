package com.gogomaya.client.game.service;

import com.gogomaya.game.Game;
import com.gogomaya.game.configuration.GameSpecificationOptions;
import com.gogomaya.game.service.GameSpecificationService;

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
