package com.gogomaya.server.integration.game;

import java.util.List;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.player.Player;

public interface GameOperations<State extends GameState> {

    @Given("selects specification")
    @When("selects specification")
    public GameSpecification selectSpecification();

    @Given("$Player selects specification")
    @When("$Player selects specification")
    public GameSpecification selectSpecification(Player player);

    @Given("options")
    @When("options")
    public GameSpecificationOptions getOptions();

    @Given("$Player options")
    @When("$Player options")
    public GameSpecificationOptions getOptions(Player player);

    public GameTable<State> start(Player player);

    @Given("$Player plays $Specification")
    @When("$Player plays $Specification")
    public GameTable<State> start(Player player, GameSpecification gameSpecification);

    public GamePlayer<State> construct(GameSpecification gameSpecification);

    public GamePlayer<State> construct(Player player);

    public GamePlayer<State> construct(Player player, GameSpecification gameSpecification);

    public List<GamePlayer<State>> constructGame();

    public List<GamePlayer<State>> constructGame(GameSpecification specification);

}
