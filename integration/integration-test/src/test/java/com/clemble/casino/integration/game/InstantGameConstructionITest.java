package com.clemble.casino.integration.game;

import static org.junit.Assert.assertEquals;

import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.GameConfigurationUtils;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.game.GameConstructionOperations;
import com.clemble.casino.game.lifecycle.construction.GameConstruction;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class InstantGameConstructionITest {

    @Autowired
    public GameScenarios gameScenarios;

    @Autowired
    public PlayerScenarios playerOperations;

    @Test
    public void testTwoPlayersInstantMatch() {
        ClembleCasinoOperations A = playerOperations.createPlayer();
        ClembleCasinoOperations B = playerOperations.createPlayer();

        GameConstructionOperations constructionOperations = A.gameConstructionOperations();
        List<GameConfiguration> allConfigurations = constructionOperations.getConfigurations();
        RoundGameConfiguration specification = GameConfigurationUtils.matchConfigurations(allConfigurations).get(0);
        GameConstruction constA = constructionOperations.constructAutomatch(specification);

        GameConstruction constB = B.gameConstructionOperations().constructAutomatch(specification);
        assertEquals(constA.getSessionKey(), constB.getSessionKey());
    }

}
