package com.clemble.casino.integration.game;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.rule.construct.PlayerNumberRule;
import com.clemble.casino.game.rule.construct.PrivacyRule;
import com.clemble.casino.game.rule.pot.PotFillRule;
import com.clemble.casino.game.rule.time.MoveTimeRule;
import com.clemble.casino.game.rule.time.TimeBreachPunishment;
import com.clemble.casino.game.rule.time.TotalTimeRule;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.GameConfigurationKey;
import com.clemble.casino.game.specification.GameConfigurations;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class PotGameConstructionITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Autowired
    public GameScenarios gameScenarios;

    @Autowired
    public ObjectMapper objectMapper;

    @Test
    public void testConstruction() throws JsonProcessingException{
        GameConfigurations allConfiguration = playerScenarios.createPlayer().gameConstructionOperations().getConfigurations();
        List<GameConfiguration> configurations = new ArrayList<>();
        MatchGameConfiguration configuration = allConfiguration.matchConfigurations().get(0);
        configurations.add(configuration);
        configurations.add(configuration);
        configurations.add(configuration);

        PotGameConfiguration potConfiguration = new PotGameConfiguration(new GameConfigurationKey(Game.pot, "pot"), Money.create(Currency.FakeMoney, 200), PrivacyRule.everybody, PlayerNumberRule.two, PotFillRule.maxcommon, new MoveTimeRule(50000, TimeBreachPunishment.loose), new TotalTimeRule(500000, TimeBreachPunishment.loose), configurations);

        System.out.println(objectMapper.writeValueAsString(potConfiguration));
    }
    
    @Test
    @Ignore
    public void construct() {
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        ClembleCasinoOperations B = playerScenarios.createPlayer();
        // Step 1. Constructing game session player
        PotGameConfiguration potConfiguration = A.gameConstructionOperations().getConfigurations().potConfigurations().get(0);
        MatchGamePlayer<NumberState> AvsB = gameScenarios.match(potConfiguration, A, B.getPlayer());
        MatchGamePlayer<NumberState> BvsA = gameScenarios.accept(AvsB.getSession(), B);
        // Step 3. First game A wins
        AvsB.waitForStart();
        BvsA.waitForStart();
        AvsB.giveUp();
        // Step 4. Second game B wins
        AvsB.waitForStart();
        BvsA.waitForStart();
        BvsA.giveUp();
        // Step 5. Second game A wins
        AvsB.waitForStart();
        BvsA.waitForStart();
        AvsB.giveUp();
    }
}
