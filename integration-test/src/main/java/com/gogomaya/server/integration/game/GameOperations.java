package com.gogomaya.server.integration.game;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;

import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.player.Player;

public interface GameOperations {

    @Given("selects specification")
    @When("selects specification")
    public GameSpecification selectSpecification();

    @Given("$Player selects specification")
    @When("$Player selects specification")
    public GameSpecification selectSpecification(Player player);

    @Given("options")
    @When("options")
    public SelectSpecificationOptions getOptions();

    @Given("$Player options")
    @When("$Player options")
    public SelectSpecificationOptions getOptions(Player player);

    @Given("$Player plays $Specification")
    @When("$Player plays $Specification")
    public <T extends GameTable<?>> T start(Player player, GameSpecification gameSpecification);

}
