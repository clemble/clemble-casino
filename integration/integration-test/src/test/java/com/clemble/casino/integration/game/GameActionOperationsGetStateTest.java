package com.clemble.casino.integration.game;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.client.game.GameConstructionOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.configuration.GameSpecificationOptions;
import com.clemble.casino.game.configuration.SelectSpecificationOptions;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class GameActionOperationsGetStateTest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Autowired
    public GameScenarios gameScenarios;

    @Test
    public void automaticConstruction() {
        // Step 1. Creating random player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        ClembleCasinoOperations B = playerScenarios.createPlayer();
        // Step 2. Constructing automatch game
        GameConstructionOperations<NumberState> constructionOperations = A.gameConstructionOperations(Game.num);
        GameSpecificationOptions specificationOptions = constructionOperations.get();
        GameSpecification specification = ((SelectSpecificationOptions) specificationOptions).getSpecifications().get(0);
        GameConstruction construction = constructionOperations.constructAutomatch(specification);
        // Step 3. Checkin getState works fine
        GameActionOperations<NumberState> aoA = A.gameActionOperations(construction.getSession());
        assertNull(aoA.getState());
        // Step 4. Creating construction from B side
        B.gameConstructionOperations(Game.num).constructAutomatch(specification);
        // Step 5. Checking value is not null anymore
        gameScenarios.construct(construction.getSession(), A).waitForStart();
        assertNotNull(aoA.getState());
    }

}
