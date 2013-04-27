package com.gogomaya.server.game.session;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.game.match.TicTacToeSpecificationRepository;
import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.game.table.TicTacToeTableRepository;
import com.gogomaya.server.game.tictactoe.TicTacToeSession;
import com.gogomaya.server.game.tictactoe.TicTacToeSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.TicTacToeStateFactory;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.spring.game.TicTacToeSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { TicTacToeSpringConfiguration.class })
public class GameSessionRepositoryTest {

    @Inject
    TicTacToeSessionRepository sessionRepository;

    @Inject
    TicTacToeTableRepository tableRepository;

    @Inject
    GameTableManager<?> tableManager;

    @Inject
    TicTacToeStateFactory stateFactory;

    @Inject
    TicTacToeSpecificationRepository specificationRepository;

    @Before
    public void setUp() {
        specificationRepository.saveAndFlush(TicTacToeSpecification.DEFAULT);
    }

    @Test
    public void testSaveSessionWithState() {
        Set<Long> players = new HashSet<Long>();
        players.add(1L);
        players.add(2L);

        TicTacToeState gameState = stateFactory.initialize(TicTacToeSpecification.DEFAULT, players);

        TicTacToeTable gameTable = new TicTacToeTable();
        gameTable.setSpecification(TicTacToeSpecification.DEFAULT);
        gameTable.setPlayers(players);

        gameTable = tableRepository.save(gameTable);

        gameTable.setState(gameState);

        TicTacToeSession gameSession = new TicTacToeSession();
        gameSession.setPlayers(players);
        gameSession.setTable(gameTable);

        gameSession = sessionRepository.save(gameSession);

        Assert.assertNotNull(gameSession);
        Assert.assertNotNull(gameTable.getState());
    }
}
