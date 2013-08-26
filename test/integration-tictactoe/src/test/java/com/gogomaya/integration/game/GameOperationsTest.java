package com.gogomaya.integration.game;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.data.DataGenerator;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.player.listener.PlayerListenerOperations;
import com.gogomaya.server.spring.integration.PicPacPoeTestConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.gogomaya.server.tictactoe.PicPacPoeState;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { PicPacPoeTestConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
public class GameOperationsTest {

    @Inject
    PlayerOperations playerOperations;

    @Inject
    GameConstructionOperations<PicPacPoeState> gameOperations;

    @Inject
    PlayerListenerOperations gameListenerOperations;

    @Test
    public void createWithGameSpecification() {
        // Step 1. Creating player
        Player player = playerOperations.createPlayer(DataGenerator.randomProfile());
        GameSpecification specification = selectSpecification(player, 0);
        // Step 2. Creating game table
        GameSessionPlayer<PicPacPoeState> gameTable = gameOperations.constructAutomatic(player, specification);
        Assert.assertNotNull(gameTable);
    }

    private GameSpecification selectSpecification(Player player, int id) {
        // Step 1. Game specification
        GameSpecification gameSpecification = null;
        GameSpecificationOptions specificationOptions = gameOperations.getOptions(player);
        if (specificationOptions instanceof SelectSpecificationOptions) {
            gameSpecification = ((SelectSpecificationOptions) specificationOptions).getSpecifications().get(id);
        } else {
            throw new UnsupportedOperationException("Do not support this kind of specification options");
        }
        return gameSpecification;
    }

}
