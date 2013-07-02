package com.gogomaya.server.integration.game;

import java.util.List;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.construct.GameRequest;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.player.Player;

public interface GameOperations<State extends GameState> {

    public GameSpecification selectSpecification();

    public GameSpecification selectSpecification(Player player);

    public GameSpecificationOptions getOptions();

    public GameSpecificationOptions getOptions(Player player);

    public GamePlayer<State> construct(Player player);

    public GamePlayer<State> construct(GameSpecification gameSpecification);

    public GamePlayer<State> construct(Player player, GameSpecification gameSpecification);

    public GamePlayer<State> construct(Player player, GameRequest request);

    public List<GamePlayer<State>> constructGame();

    public List<GamePlayer<State>> constructGame(GameSpecification specification);

}
