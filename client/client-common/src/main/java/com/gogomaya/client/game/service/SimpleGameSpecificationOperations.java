package com.gogomaya.client.game.service;

import com.gogomaya.game.Game;
import com.gogomaya.game.configuration.GameSpecificationOptions;
import com.gogomaya.game.service.GameSpecificationService;

public class SimpleGameSpecificationOperations implements GameSpecificationOperations {

    final private long playerId;
    final private GameSpecificationService specificationService;

    public SimpleGameSpecificationOperations(long playerId, GameSpecificationService specificationService) {
        this.playerId = playerId;
        this.specificationService = specificationService;
    }

    @Override
    public GameSpecificationOptions get(Game game) {
        return specificationService.getSpecificationOptions(playerId, game);
    }

}
