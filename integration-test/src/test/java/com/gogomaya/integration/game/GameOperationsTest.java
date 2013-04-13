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
import com.gogomaya.server.game.action.GameSessionState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.integration.data.DataGenerator;
import com.gogomaya.server.integration.game.GameListener;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.ListenerChannel;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
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
    public void createWithGameSpecification() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer(DataGenerator.randomProfile());
        SelectSpecificationOptions selectSpecificationOptions = gameOperations.getOptions(player);
        GameSpecification gameSpecification = selectSpecificationOptions.specifications.get(0);
        // Step 2. Creating game table
        GameTable gameTable = gameOperations.start(player, gameSpecification);
        Assert.assertNotNull(gameTable);
    }

    @Test
    public void createTicTacToeSpecification() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer(DataGenerator.randomProfile());
        SelectSpecificationOptions selectSpecificationOptions = gameOperations.getOptions(player);
        GameSpecification specification = selectSpecificationOptions.specifications.get(1);
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
        SelectSpecificationOptions selectSpecificationOptions = gameOperations.getOptions(player);
        GameSpecification specification = selectSpecificationOptions.specifications.get(2);
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
