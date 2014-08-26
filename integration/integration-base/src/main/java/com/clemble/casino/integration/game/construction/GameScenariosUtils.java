package com.clemble.casino.integration.game.construction;

import java.util.List;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.integration.util.RandomUtils;

public class GameScenariosUtils {

    public static RoundGameConfiguration random(ClembleCasinoOperations player, Game game) {
        return random(player.gameConstructionOperations().getConfigurations().matchConfigurations(game));
    }

    public static RoundGameConfiguration random(List<RoundGameConfiguration> specificationOptions) {
        return specificationOptions.get(RandomUtils.RANDOM.nextInt(specificationOptions.size()));
    }

}
