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
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.event.GogomayaEvent;
import com.gogomaya.server.game.action.GameSessionState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.integration.data.DataGenerator;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.listener.GameListener;
import com.gogomaya.server.integration.game.listener.GameListenerOperations;
import com.gogomaya.server.integration.game.listener.ListenerChannel;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.tests.validation.PlayerCredentialsValidation;

@RunWith(SpringJUnit4ClassRunner.class)
@UsingSteps(instances = PlayerCredentialsValidation.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
public class GameOperationsTest {

    @Inject
    PlayerOperations playerOperations;

    @Inject
    GameOperations<TicTacToeState> gameOperations;

    @Inject
    GameListenerOperations<TicTacToeState> gameListenerOperations;

    @Test
    public void createWithGameSpecification() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer(DataGenerator.randomProfile());
        GameSpecification specification = selectSpecification(player, 0);
        // Step 2. Creating game table
        GameTable<TicTacToeState> gameTable = gameOperations.start(player, specification);
        Assert.assertNotNull(gameTable);
    }

    @Test
    public void createTicTacToeSpecification() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer(DataGenerator.randomProfile());
        GameSpecification specification = selectSpecification(player, 1);
        // Step 2. Creating game table
        GameTable<TicTacToeState> gameTable = gameOperations.start(player, specification);
        Assert.assertNotNull(gameTable);
        // Step 3. Adding another player to the table
        Player anotherPlayer = playerOperations.createPlayer(DataGenerator.randomProfile());
        Assert.assertNotSame(anotherPlayer.getPlayerId(), player.getPlayerId());
        gameTable = gameOperations.start(anotherPlayer, specification);
        Assert.assertNotNull(gameTable);
        Assert.assertEquals(gameTable.getCurrentSession().getSessionState(), GameSessionState.active);
        Assert.assertNotNull(gameTable.getCurrentSession().getState());
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
        GameSpecification specification = selectSpecification(player, 2);
        // Step 2. Creating game table
        GameTable<TicTacToeState> gameTable = gameOperations.start(player, specification);
        Assert.assertNotNull(gameTable);
        // Step 3. Adding listener
        gameListenerOperations.listen(gameTable, new GameListener() {
            @Override
            public void updated(GogomayaEvent event) {
                System.out.println(event);
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

    private GameSpecification selectSpecification(Player player, int id) {
        // Step 1. Game specification
        GameSpecification gameSpecification = null;
        GameSpecificationOptions specificationOptions = gameOperations.getOptions(player);
        if (specificationOptions instanceof SelectSpecificationOptions) {
            gameSpecification = ((SelectSpecificationOptions) specificationOptions).specifications.get(id);
        } else {
            throw new UnsupportedOperationException("Do not support this kind of specification options");
        }
        return gameSpecification;
    }

}
