package com.gogomaya.server.game.session;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.table.GameTable;
import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.game.table.GameTableRepository;
import com.gogomaya.server.spring.game.GameManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { GameManagementSpringConfiguration.class })
public class GameSessionRepositoryTest {

    @Inject
    private GameSessionRepository sessionRepository;

    @Inject
    private GameTableRepository tableRepository;

    @Inject
    private GameTableManager tableManager;

    @Inject
    private GameStateFactory stateFactory;

    @Test
    public void testInitialized() {
        Assert.assertNotNull(sessionRepository);
        Assert.assertNotNull(tableManager);
    }

    @Test
    public void testSaveSessionWithState() {
        Set<Long> players = new HashSet<Long>();
        players.add(1L);
        players.add(2L);

        GameState<?, ?> gameState = stateFactory.initialize(GameSpecification.DEFAULT, players);

        GameTable gameTable = new GameTable();
        gameTable.setSpecification(GameSpecification.DEFAULT);
        gameTable.setPlayers(players);

        gameTable = tableRepository.save(gameTable);

        GameSession gameSession = new GameSession();
        gameSession.setPlayers(players);
        gameSession.setTable(gameTable);

        gameSession.setGameState(gameState);

        gameSession = sessionRepository.save(gameSession);

        Assert.assertNotNull(gameSession);
        Assert.assertNotNull(gameSession.getGameState());
    }
}
