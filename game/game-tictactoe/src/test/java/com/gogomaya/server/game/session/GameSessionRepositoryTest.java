package com.gogomaya.server.game.session;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.rule.construction.MatchRule;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.rule.construction.PrivacyRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationRepository;
import com.gogomaya.server.game.specification.SpecificationName;
import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.game.table.GameTableRepository;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.TicTacToeStateFactory;
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
    GameSessionRepository sessionRepository;

    @Inject
    GameTableRepository<TicTacToeState> tableRepository;

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
        List<Long> players = new ArrayList<Long>();
        players.add(1L);
        players.add(2L);

        TicTacToeState gameState = stateFactory.create(DEFAULT_SPECIFICATION, players);

        GameTable<TicTacToeState> gameTable = new GameTable<TicTacToeState>();
        gameTable.setSpecification(DEFAULT_SPECIFICATION);
        gameTable.setPlayers(players);

        gameTable = tableRepository.save(gameTable);

        gameTable.setState(gameState);

        GameSession gameSession = new GameSession();
        gameSession.setPlayers(players);
        gameSession.setSpecification(DEFAULT_SPECIFICATION);

        gameSession = sessionRepository.save(gameSession);

        Assert.assertNotNull(gameSession);
        Assert.assertNotNull(gameTable.getState());
    }
}
