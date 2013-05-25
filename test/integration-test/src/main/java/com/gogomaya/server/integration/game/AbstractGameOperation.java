package com.gogomaya.server.integration.game;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.player.Player;

abstract public class AbstractGameOperation<State extends GameState> implements GameOperations<State> {

    @Override
    public GameSpecification selectSpecification() {
        GameSpecificationOptions specificationOptions = getOptions();
        if (specificationOptions instanceof SelectSpecificationOptions) {
            return ((SelectSpecificationOptions) specificationOptions).getSpecifications().get(0);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public GameSpecification selectSpecification(Player player) {
        GameSpecificationOptions specificationOptions = getOptions(player);
        if (specificationOptions instanceof SelectSpecificationOptions) {
            return ((SelectSpecificationOptions) specificationOptions).getSpecifications().get(0);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
