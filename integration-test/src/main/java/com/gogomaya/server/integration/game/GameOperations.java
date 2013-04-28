package com.gogomaya.server.integration.game;

import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.player.Player;

public interface GameOperations {

    public SelectSpecificationOptions getOptions();

    public SelectSpecificationOptions getOptions(Player player);

    public <T extends GameTable<?>> T start(Player player, GameSpecification gameSpecification);

}
