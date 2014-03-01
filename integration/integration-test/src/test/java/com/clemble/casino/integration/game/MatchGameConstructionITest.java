package com.clemble.casino.integration.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.rule.construct.PlayerNumberRule;
import com.clemble.casino.game.rule.construct.PrivacyRule;
import com.clemble.casino.game.rule.outcome.DrawRule;
import com.clemble.casino.game.rule.outcome.WonRule;
import com.clemble.casino.game.rule.pot.MatchFillRule;
import com.clemble.casino.game.rule.time.MoveTimeRule;
import com.clemble.casino.game.rule.time.TimeBreachPunishment;
import com.clemble.casino.game.rule.time.TotalTimeRule;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.GameConfigurationKey;
import com.clemble.casino.game.specification.GameConfigurations;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.RoundGameConfiguration;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class MatchGameConstructionITest {

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
        RoundGameConfiguration configuration = allConfiguration.matchConfigurations().get(0);
        configurations.add(configuration);
        configurations.add(configuration);
        configurations.add(configuration);

        MatchGameConfiguration potConfiguration = new MatchGameConfiguration(new GameConfigurationKey(Game.pot, "pot"), Money.create(Currency.FakeMoney, 200), PrivacyRule.everybody, PlayerNumberRule.two, MatchFillRule.maxcommon, new MoveTimeRule(50000, TimeBreachPunishment.loose), new TotalTimeRule(500000, TimeBreachPunishment.loose), WonRule.owned, DrawRule.owned, configurations);

        System.out.println(objectMapper.writeValueAsString(potConfiguration));
    }

    @Test
    public void testIsAlive() {
        List<MatchGamePlayer> potPlayers = gameScenarios.pot();
        // Step 1. Constructing game session player
        MatchGamePlayer AvsB = potPlayers.get(0);
        MatchGamePlayer BvsA = potPlayers.get(1);
        // Step 2. Checking pot get to live
        assertTrue(AvsB.isAlive());
        assertTrue(BvsA.isAlive());
    }

    @Test
    public void testActiveSession() {
        List<MatchGamePlayer> potPlayers = gameScenarios.pot();
        // Step 1. Constructing game session player
        MatchGamePlayer AvsB = potPlayers.get(0);
        MatchGamePlayer BvsA = potPlayers.get(1);
        AvsB.waitForStart();
        // Step 2. Checking pot get to live
        // Step 2.1. Giving up 2 games at a row
        GamePlayer currentAvsB = AvsB.getСurrent();
        currentAvsB.waitForStart();
        assertTrue(currentAvsB.isAlive());
        currentAvsB.giveUp();

        AvsB.waitVersion(1);

        currentAvsB = AvsB.getСurrent();
        currentAvsB.waitForStart();
        assertTrue(currentAvsB.isAlive());
        currentAvsB.giveUp();

        AvsB.waitVersion(2);

        // Step 3. Checking there is a payment transaction
        PaymentTransaction transaction = BvsA.playerOperations().paymentOperations().getPaymentTransaction(AvsB.getSession());
        assertNotNull(transaction);

        Money mA = AvsB.playerOperations().paymentOperations().getAccount().getMoney(Currency.FakeMoney);
        Money mB = BvsA.playerOperations().paymentOperations().getAccount().getMoney(Currency.FakeMoney);

        assertEquals(mB.getAmount(), mA.add(300).getAmount());
    }
}
