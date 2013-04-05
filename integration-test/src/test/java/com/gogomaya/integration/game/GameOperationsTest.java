package com.gogomaya.integration.game;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.jbehave.core.annotations.UsingSteps;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.rule.GameRuleSpecification;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.rule.giveup.LooseAllGiveUpRule;
import com.gogomaya.server.game.rule.time.UnlimitedTimeRule;
import com.gogomaya.server.game.session.GameSessionState;
import com.gogomaya.server.game.table.GameTable;
import com.gogomaya.server.game.table.rule.GameTableSpecification;
import com.gogomaya.server.game.table.rule.PlayerMatchRule;
import com.gogomaya.server.game.table.rule.PlayerNumberRule;
import com.gogomaya.server.game.table.rule.PlayerPrivacyRule;
import com.gogomaya.server.integration.data.DataGenerator;
import com.gogomaya.server.integration.game.GameListener;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.ListenerChannel;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.player.wallet.CashType;
import com.gogomaya.server.spring.integration.IntegrationTestConfiguration;
import com.gogomaya.tests.validation.PlayerCredentialsValidation;

@RunWith(SpringJUnit4ClassRunner.class)
@UsingSteps(instances = PlayerCredentialsValidation.class)
@ContextConfiguration(classes = { IntegrationTestConfiguration.class })
public class GameOperationsTest {

    @Inject
    PlayerOperations playerOperations;

    @Inject
    GameOperations gameOperations;

    @Test
    public void createWithoutGameSpecifiaction() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer(DataGenerator.randomProfile());
        // Step 2. Creating game table
        GameTable gameTable = gameOperations.create(player);
        Assert.assertNotNull(gameTable);
    }

    @Test
    public void createWithGameSpecification() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer(DataGenerator.randomProfile());
        GameSpecification gameSpecification = GameSpecification.create(
                GameTableSpecification.create(PlayerMatchRule.Automatic, PlayerPrivacyRule.Public, PlayerNumberRule.create(2, 2)),
                GameRuleSpecification.DEFAULT_RULE_SPECIFICATION);
        // Step 2. Creating game table
        GameTable gameTable = gameOperations.create(player, gameSpecification);
        Assert.assertNotNull(gameTable);
    }

    @Test
    public void createTicTacToeSpecification() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer(DataGenerator.randomProfile());
        GameSpecification specification = GameSpecification.create(
                GameTableSpecification.create(PlayerMatchRule.Automatic, PlayerPrivacyRule.Public, PlayerNumberRule.create(2, 2)),
                GameRuleSpecification.create(CashType.FakeMoney, FixedBetRule.create(50), LooseAllGiveUpRule.INSTANCE, UnlimitedTimeRule.INSTANCE));
        // Step 2. Creating game table
        GameTable gameTable = gameOperations.create(player, specification);
        Assert.assertNotNull(gameTable);
        // Step 3. Adding another player to the table
        Player anotherPlayer = playerOperations.createPlayer(DataGenerator.randomProfile());
        Assert.assertNotSame(anotherPlayer.getPlayerId(), player.getPlayerId());
        gameTable = gameOperations.create(anotherPlayer, specification);
        Assert.assertNotNull(gameTable);
        Assert.assertEquals(gameTable.getCurrentSession().getSessionState(), GameSessionState.Active);
        Assert.assertNotNull(gameTable.getCurrentSession().getGameState());
    }

    @Test
    public void createAndListenRabbit() {
        createAndListen(ListenerChannel.Rabbit);
    }

    @Test
    public void createAndListenStomp() {
        createAndListen(ListenerChannel.Stomp);
    }

    public void createAndListen(ListenerChannel listenerChannel) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        // Step 1. Creating player
        Player player = playerOperations.createPlayer(DataGenerator.randomProfile());
        GameSpecification specification = GameSpecification.create(
                GameTableSpecification.create(PlayerMatchRule.Automatic, PlayerPrivacyRule.Public, PlayerNumberRule.create(2, 2)),
                GameRuleSpecification.DEFAULT_RULE_SPECIFICATION);
        // Step 2. Creating game table
        GameTable gameTable = gameOperations.create(player, specification);
        Assert.assertNotNull(gameTable);
        // Step 3. Adding listener
        gameOperations.addListener(gameTable, new GameListener() {
            @Override
            public void updated(GameTable gameTable) {
                System.out.println(gameTable);
                countDownLatch.countDown();
            }
        }, listenerChannel);
        // Step 4. Adding another player to the table
        player = playerOperations.createPlayer(DataGenerator.randomProfile());
        gameTable = gameOperations.create(player, specification);
        // Step 5. Waiting for notification to happen
        try {
            countDownLatch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
        Assert.assertEquals(countDownLatch.getCount(), 0);
    }

}
