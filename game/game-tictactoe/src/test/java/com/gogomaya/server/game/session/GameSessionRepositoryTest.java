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

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.SpecificationName;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.rule.construction.MatchRule;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.rule.construction.PrivacyRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.game.specification.GameSpecificationRepository;
import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.game.table.TicTacToeTableRepository;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.TicTacToeStateFactory;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.spring.game.TicTacToeSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { TicTacToeSpringConfiguration.class })
public class GameSessionRepositoryTest {

    GameSpecification DEFAULT_SPECIFICATION = new GameSpecification().setName(new SpecificationName("DEFAULT", "")).setBetRule(new FixedBetRule(50))
            .setCurrency(Currency.FakeMoney).setGiveUpRule(GiveUpRule.lost).setMatchRule(MatchRule.automatic).setMoveTimeRule(MoveTimeRule.DEFAULT)
            .setTotalTimeRule(TotalTimeRule.DEFAULT).setNumberRule(PlayerNumberRule.two).setPrivacayRule(PrivacyRule.everybody);;

    @Inject
    TicTacToeSessionRepository sessionRepository;

    @Inject
    TicTacToeTableRepository tableRepository;

    @Inject
    GameTableManager<?> tableManager;

    @Inject
    TicTacToeStateFactory stateFactory;

    @Inject
    GameSpecificationRepository specificationRepository;

    @Before
    public void setUp() {
        specificationRepository.saveAndFlush(DEFAULT_SPECIFICATION);
    }

    @Test
    public void testSaveSessionWithState() {
        Set<Long> players = new HashSet<Long>();
        players.add(1L);
        players.add(2L);

        TicTacToeState gameState = stateFactory.initialize(DEFAULT_SPECIFICATION, players);

        TicTacToeTable gameTable = new TicTacToeTable();
        gameTable.setSpecification(DEFAULT_SPECIFICATION);
        gameTable.setPlayers(players);

        gameTable = tableRepository.save(gameTable);

        gameTable.setState(gameState);

        GameSession<TicTacToeState> gameSession = new GameSession<TicTacToeState>();
        gameSession.setPlayers(players);
        gameSession.setSpecification(DEFAULT_SPECIFICATION);

        gameSession = sessionRepository.save(gameSession);

        Assert.assertNotNull(gameSession);
        Assert.assertNotNull(gameTable.getState());
    }
}
