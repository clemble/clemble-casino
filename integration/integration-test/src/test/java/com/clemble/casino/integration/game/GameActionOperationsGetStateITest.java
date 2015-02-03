package com.clemble.casino.integration.game;

import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.GameConfigurationUtils;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.integration.ClembleIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.client.game.GameConstructionOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.game.lifecycle.construction.GameConstruction;
import com.clemble.casino.game.lifecycle.initiation.event.GameInitiationCanceledEvent;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.server.game.construction.service.ServerGameInitiationService;
import com.clemble.test.concurrent.AsyncCompletionUtils;
import com.clemble.test.concurrent.Check;
import com.clemble.test.concurrent.Get;

@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class GameActionOperationsGetStateITest {

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
        GameConstructionOperations constructionOperations = A.gameConstructionOperations();

        List<GameConfiguration> allConfigurations = constructionOperations.getConfigurations();
        List<RoundGameConfiguration> configurations = GameConfigurationUtils.matchConfigurations(Game.num, allConfigurations);

        RoundGameConfiguration configuration = configurations.get(0);
        GameConstruction construction = constructionOperations.constructAutomatch(configuration);
        // Step 3. Checking getState works fine
        final GameActionOperations<RoundGameState> aoA = A.gameActionOperations(construction.getSessionKey());
        assertNull(aoA.getState());
        // Step 4. Creating construction from B side
        B.gameConstructionOperations().constructAutomatch(configuration);
        // Step 5. Checking value is not null anymore
        gameScenarios.round(construction.getSessionKey(), A).waitForStart();
        AsyncCompletionUtils.<GameState>get(new Get<GameState>() {
            @Override
            public GameState get() {
                return aoA.getState();
            }
        }, 5_000);
    }

    @Test
    public void automaticConstructionWithDisabled() {
        // Step 1. Creating random player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        ClembleCasinoOperations B = playerScenarios.createPlayer();
        // Step 1.1. Accumulating Game initiation events
        final EventAccumulator<GameInitiationCanceledEvent> listener = new EventAccumulator<>();
        B.listenerOperations().subscribe(new EventTypeSelector(GameInitiationCanceledEvent.class), listener);
        // Step 2. Constructing automatch game
        GameConstructionOperations constructionOperations = A.gameConstructionOperations();

        List<GameConfiguration> allConfigurations = constructionOperations.getConfigurations();
        List<RoundGameConfiguration> specificationOptions = GameConfigurationUtils.matchConfigurations(Game.num, allConfigurations);

        RoundGameConfiguration specification = specificationOptions.get(0);
        constructionOperations.constructAutomatch(specification);
        // Step 3. Checking getState works fine
        A.close();
        // Step 4. Creating construction from B side
        B.gameConstructionOperations().constructAutomatch(specification);
        // Step 5. Checking value is not null anymore
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(ServerGameInitiationService.CANCEL_TIMEOUT_SECONDS));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        AsyncCompletionUtils.check(new Check() {
            @Override
            public boolean check() {
                return listener.toList().size() == 1;
            }
        }, 10_000);
    }

}
