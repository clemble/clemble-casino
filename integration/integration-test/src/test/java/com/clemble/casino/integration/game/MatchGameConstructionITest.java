package com.clemble.casino.integration.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.configuration.GameConfigurationUtils;
import com.clemble.casino.integration.player.ClembleCasinoRegistrationOperationsWrapper;
import com.clemble.casino.rule.breach.LooseBreachPunishment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.rule.construct.PlayerNumberRule;
import com.clemble.casino.rule.privacy.PrivacyRule;
import com.clemble.casino.game.rule.outcome.DrawRule;
import com.clemble.casino.game.rule.outcome.WonRule;
import com.clemble.casino.game.rule.match.MatchFillRule;
import com.clemble.casino.rule.time.MoveTimeRule;
import com.clemble.casino.rule.time.TotalTimeRule;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.game.configuration.MatchGameConfiguration;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
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
        List<GameConfiguration> allConfiguration = playerScenarios.createPlayer().gameConstructionOperations().getConfigurations();
        List<GameConfiguration> configurations = new ArrayList<>();
        RoundGameConfiguration configuration = GameConfigurationUtils.matchConfigurations(allConfiguration).get(0);
        configurations.add(configuration);
        configurations.add(configuration);
        configurations.add(configuration);

        MatchGameConfiguration matchConfiguration = new MatchGameConfiguration(Game.pot, "pot", Money.create(Currency.FakeMoney, 200), PrivacyRule.everybody, PlayerNumberRule.two, MatchFillRule.maxcommon, new MoveTimeRule(50000, LooseBreachPunishment.getInstance()), new TotalTimeRule(500000, LooseBreachPunishment.getInstance()), WonRule.owned, DrawRule.owned, configurations, null);

        System.out.println(objectMapper.writeValueAsString(matchConfiguration));
    }

    @Test
    public void testIsAlive() {
        List<MatchGamePlayer> potPlayers = gameScenarios.match();
        // Step 1. Constructing game session player
        MatchGamePlayer AvsB = potPlayers.get(0);
        MatchGamePlayer BvsA = potPlayers.get(1);
        // Step 2. Checking pot get to live
        assertTrue(AvsB.isAlive());
        assertTrue(BvsA.isAlive());
    }

    @Test
    public void testActiveSession() {
        List<MatchGamePlayer> potPlayers = gameScenarios.match();
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
        PaymentTransaction transaction = BvsA.playerOperations().paymentOperations().getTransaction(AvsB.getSessionKey());
        assertNotNull(transaction);

        Money mA = AvsB.playerOperations().accountService().myAccount().getMoney(Currency.FakeMoney);
        Money mB = BvsA.playerOperations().accountService().myAccount().getMoney(Currency.FakeMoney);

        assertEquals("Amount does not match for " + AvsB.getPlayer(), mB.getAmount(), mA.add(300).getAmount());

        GameRecord record = AvsB.playerOperations().gameRecordOperations().get(AvsB.getSessionKey());
        assertEquals(record.getEventRecords().size(), 6);
    }
}
