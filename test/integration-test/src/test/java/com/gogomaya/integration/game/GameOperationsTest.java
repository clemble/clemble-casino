package com.gogomaya.integration.game;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.event.Event;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.integration.data.DataGenerator;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.GamePlayer;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.player.listener.ListenerChannel;
import com.gogomaya.server.integration.player.listener.PlayerListener;
import com.gogomaya.server.integration.player.listener.PlayerListenerOperations;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
public class GameOperationsTest {

    @Inject
    PlayerOperations playerOperations;

    @Inject
    GameOperations<TicTacToeState> gameOperations;

    @Inject
    PlayerListenerOperations gameListenerOperations;

    @Test
    public void createWithGameSpecification() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer(DataGenerator.randomProfile());
        GameSpecification specification = selectSpecification(player, 0);
        // Step 2. Creating game table
        GamePlayer<TicTacToeState> gameTable = gameOperations.construct(player, specification);
        Assert.assertNotNull(gameTable);
        gameOperations.construct(specification);
    }

    @Test
    public void createTicTacToeSpecification() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer(DataGenerator.randomProfile());
        GameSpecification specification = selectSpecification(player, 1);
        // Step 2. Creating game table
        GamePlayer<TicTacToeState> gamePlayer = gameOperations.construct(player, specification);
        Assert.assertNotNull(gamePlayer);
        // Step 3. Adding another player to the table
        Player anotherPlayer = playerOperations.createPlayer(DataGenerator.randomProfile());
        Assert.assertNotSame(anotherPlayer.getPlayerId(), player.getPlayerId());
        gamePlayer = gameOperations.construct(anotherPlayer, specification);
        Assert.assertNotNull(gamePlayer);
        Assert.assertNotNull(gamePlayer.getServerResourse());
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
        // Step 2. Adding listener
        gameListenerOperations.listen(player.getSession(), new PlayerListener() {
            @Override
            public void updated(Event event) {
                System.out.println(event);
                countDownLatch.countDown();
            }
        }, listenerChannel);
        // Step 2. Creating game table
        GameSpecification specification = selectSpecification(player, 2);
        GamePlayer<TicTacToeState> gameTable = gameOperations.construct(player, specification);
        Assert.assertNotNull(gameTable);
        // Step 4. Adding another player to the table
        player = playerOperations.createPlayer(DataGenerator.randomProfile());
        GamePlayer<TicTacToeState> anotherTable = gameOperations.construct(player, specification);
        Assert.assertEquals(anotherTable.getTableId(), gameTable.getTableId());
        // Step 5. Waiting for notification to happen
        try {
            countDownLatch.await(5, TimeUnit.SECONDS);
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
