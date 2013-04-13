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
import com.gogomaya.server.game.SpecificationName;
import com.gogomaya.server.game.action.GameSessionState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.rule.construction.MatchRule;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.rule.construction.PrivacyRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.game.tictactoe.TicTacToeSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.integration.data.DataGenerator;
import com.gogomaya.server.integration.game.GameListener;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.ListenerChannel;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.money.Currency;
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
        GameTable gameTable = gameOperations.start(player);
        Assert.assertNotNull(gameTable);
    }

    @Test
    public void createWithGameSpecification() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer(DataGenerator.randomProfile());
        GameSpecification gameSpecification = TicTacToeSpecification.DEFAULT;
        // Step 2. Creating game table
        GameTable gameTable = gameOperations.start(player, gameSpecification);
        Assert.assertNotNull(gameTable);
    }

    @Test
    public void createTicTacToeSpecification() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer(DataGenerator.randomProfile());
        GameSpecification specification = new TicTacToeSpecification().setCurrency(Currency.FakeMoney).setBetRule(new FixedBetRule(50))
                .setGiveUpRule(GiveUpRule.DEFAULT).setTotalTimeRule(TotalTimeRule.DEFAULT).setMoveTimeRule(MoveTimeRule.DEFAULT)
                .setMatchRule(MatchRule.automatic).setPrivacayRule(PrivacyRule.players).setNumberRule(PlayerNumberRule.TWO)
                .setName(new SpecificationName("test", "test"));
        // Step 2. Creating game table
        TicTacToeTable gameTable = gameOperations.start(player, specification);
        Assert.assertNotNull(gameTable);
        // Step 3. Adding another player to the table
        Player anotherPlayer = playerOperations.createPlayer(DataGenerator.randomProfile());
        Assert.assertNotSame(anotherPlayer.getPlayerId(), player.getPlayerId());
        gameTable = gameOperations.start(anotherPlayer, specification);
        Assert.assertNotNull(gameTable);
        Assert.assertEquals(gameTable.getCurrentSession().getSessionState(), GameSessionState.ACTIVE);
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
        GameSpecification specification = TicTacToeSpecification.DEFAULT;
        // Step 2. Creating game table
        GameTable gameTable = gameOperations.start(player, specification);
        Assert.assertNotNull(gameTable);
        // Step 3. Adding listener
        gameOperations.listen(gameTable, new GameListener() {
            @Override
            public void updated(GameTable gameTable) {
                System.out.println(gameTable);
                countDownLatch.countDown();
            }
        }, listenerChannel);
        // Step 4. Adding another player to the table
        player = playerOperations.createPlayer(DataGenerator.randomProfile());
        gameTable = gameOperations.start(player, specification);
        // Step 5. Waiting for notification to happen
        try {
            countDownLatch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
        Assert.assertEquals(countDownLatch.getCount(), 0);
    }

}
