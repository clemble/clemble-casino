package com.gogomaya.server.game.table;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.server.game.SpecificationName;
import com.gogomaya.server.game.match.TicTacToeSpecificationRepository;
import com.gogomaya.server.game.match.GameMatchingServiceImpl;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.rule.construction.MatchRule;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.rule.construction.PrivacyRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.game.tictactoe.TicTacToeSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.spring.game.TicTacToeSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { TicTacToeSpringConfiguration.class })
@Transactional
public class GameTableManagerTest {

    @Inject
    GameMatchingServiceImpl<TicTacToeState, TicTacToeSpecification> gameStateManager;

    @Inject
    TicTacToeSpecificationRepository specificationRepository;

    @Test
    public void testPlayersMapping() {
        TicTacToeSpecification specification = new TicTacToeSpecification().setName(new SpecificationName("", "")).setCurrency(Currency.FakeMoney)
                .setBetRule(new FixedBetRule(50)).setGiveUpRule(GiveUpRule.DEFAULT).setTotalTimeRule(TotalTimeRule.DEFAULT)
                .setMoveTimeRule(MoveTimeRule.DEFAULT).setMatchRule(MatchRule.automatic).setPrivacayRule(PrivacyRule.players)
                .setNumberRule(PlayerNumberRule.two);

        specificationRepository.saveAndFlush(specification);

        TicTacToeTable table = (TicTacToeTable) gameStateManager.reserve(1, specification);

        Assert.assertEquals(table.getSpecification(), specification);

        TicTacToeTable anotherTable = (TicTacToeTable) gameStateManager.reserve(2, specification);

        Assert.assertEquals(table.getTableId(), anotherTable.getTableId());
    }
}
