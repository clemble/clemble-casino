package com.clemble.casino.integration.game.construction;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.configuration.GameSpecificationOptions;
import com.clemble.casino.game.configuration.SelectSpecificationOptions;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.util.RandomUtils;

public class GameScenariosUtils {

    public static GameSpecification random(ClembleCasinoOperations player, Game game) {
        return random(player.gameConstructionOperations(game).get());
    }

    public static GameSpecification random(GameSpecificationOptions specificationOptions) {
        if (specificationOptions instanceof SelectSpecificationOptions) {
            return ((SelectSpecificationOptions) specificationOptions).getSpecifications().get(
                    RandomUtils.RANDOM.nextInt(((SelectSpecificationOptions) specificationOptions).getSpecifications().size()));
        } else {
            throw new UnsupportedOperationException("This specification options not supported " + specificationOptions);
        }
    }

}
