package com.clemble.casino.integration.game.construction;

import java.util.List;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.integration.util.RandomUtils;

public class GameScenariosUtils {

    public static MatchGameConfiguration random(ClembleCasinoOperations player, Game game) {
        return random(player.gameConstructionOperations(game).getConfigurations().matchConfigurations());
    }

    public static MatchGameConfiguration random(List<MatchGameConfiguration> specificationOptions) {
        return specificationOptions.get(RandomUtils.RANDOM.nextInt(specificationOptions.size()));
    }

}
