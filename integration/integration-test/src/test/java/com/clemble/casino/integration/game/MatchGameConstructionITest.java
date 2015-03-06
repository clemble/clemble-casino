package com.clemble.casino.integration.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.clemble.casino.game.lifecycle.record.GameRecord;
import com.clemble.casino.game.lifecycle.configuration.GameConfigurationUtils;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.utils.AsyncUtils;
import com.clemble.casino.lifecycle.configuration.rule.breach.LooseBreachPunishment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.lifecycle.configuration.rule.construct.PlayerNumberRule;
import com.clemble.casino.game.lifecycle.configuration.rule.outcome.DrawRule;
import com.clemble.casino.game.lifecycle.configuration.rule.outcome.WonRule;
import com.clemble.casino.game.lifecycle.configuration.rule.match.MatchFillRule;
import com.clemble.casino.lifecycle.configuration.rule.time.MoveTimeRule;
import com.clemble.casino.lifecycle.configuration.rule.time.TotalTimeRule;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
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

        MatchGameConfiguration matchConfiguration = new MatchGameConfiguration(Game.pot, "pot", Money.create(Currency.point, 200), PlayerNumberRule.two, MatchFillRule.maxcommon, new MoveTimeRule(50000, LooseBreachPunishment.getInstance()), new TotalTimeRule(500000, LooseBreachPunishment.getInstance()), WonRule.owned, DrawRule.owned, configurations, null);

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
        final MatchGamePlayer AvsB = potPlayers.get(0);
        final MatchGamePlayer BvsA = potPlayers.get(1);

        // Game is calculated with win for each game + pot rule aggregated
        // So not all money can be spent in a game

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

        // Processing is async, so payment might complete only after certain delay
        boolean check = AsyncUtils.check((test) -> {
            Money mA = AvsB.playerOperations().accountService().myAccount().getMoney(Currency.point);
            Money mB = BvsA.playerOperations().accountService().myAccount().getMoney(Currency.point);

            long mBAmount = mB.getAmount();
            long expectedAmount = mA.add(300).getAmount();

            return mBAmount == expectedAmount;
        });
        assertTrue(check);

        Money mA = AvsB.playerOperations().accountService().myAccount().getMoney(Currency.point);
        Money mB = BvsA.playerOperations().accountService().myAccount().getMoney(Currency.point);

        long mBAmount = mB.getAmount();
        long expectedAmount = mA.add(300).getAmount();
        assertEquals("Amount does not match for " + AvsB.getPlayer(), mBAmount, expectedAmount);

        GameRecord record = AvsB.playerOperations().gameRecordOperations().get(AvsB.getSessionKey());
        assertEquals(record.getEventRecords().size(), 5);
    }
}
