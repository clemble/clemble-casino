package com.gogomaya.server.game.table;

import java.io.IOException;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.build.GameConstructionService;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.rule.construction.PrivacyRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationRepository;
import com.gogomaya.server.game.specification.SpecificationName;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.spring.tictactoe.TicTacToeSpringConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.gogomaya.server.tictactoe.TicTacToeState;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { TicTacToeSpringConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class GameTableManagerTest {

    @Inject
    GameConstructionService<TicTacToeState> gameStateManager;

    @Inject
    GameSpecificationRepository specificationRepository;

    @Inject
    ObjectMapper objectMapper;

    @Test
    public void testPlayersMapping() throws JsonGenerationException, JsonMappingException, IOException {
        GameSpecification specification = new GameSpecification().setName(new SpecificationName("", "")).setCurrency(Currency.FakeMoney)
                .setBetRule(new FixedBetRule(50)).setGiveUpRule(GiveUpRule.DEFAULT).setTotalTimeRule(TotalTimeRule.DEFAULT)
                .setMoveTimeRule(MoveTimeRule.DEFAULT).setPrivacayRule(PrivacyRule.players)
                .setNumberRule(PlayerNumberRule.two);

        specificationRepository.saveAndFlush(specification);

        GameTable<TicTacToeState> table = gameStateManager.instantGame(1, specification);

        String serialized = objectMapper.writeValueAsString(table);
        objectMapper.readValue(serialized, GameTable.class);

        Assert.assertEquals(table.getSpecification(), specification);

        GameTable<TicTacToeState> anotherTable = gameStateManager.instantGame(2, specification);

        if (table.getTableId() != anotherTable.getTableId()) {
            anotherTable = gameStateManager.instantGame(3, specification);
        }

        Assert.assertEquals(table.getTableId(), anotherTable.getTableId());
    }
}
