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
import com.gogomaya.server.game.construct.GameConstructionService;
import com.gogomaya.server.game.construct.InstantGameRequest;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.rule.construction.PlayerNumberRule;
import com.gogomaya.server.game.rule.construction.PrivacyRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRule;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationRepository;
import com.gogomaya.server.game.specification.SpecificationName;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.spring.tictactoe.TicTacToeSpringConfiguration;
import com.gogomaya.server.test.RedisCleaner;

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

        InstantGameRequest instantGameRequest = new InstantGameRequest();
        instantGameRequest.setSpecification(specification);
        instantGameRequest.setPlayerId(1);
        GameTable<TicTacToeState> table = gameStateManager.construct(instantGameRequest);

        String serialized = objectMapper.writeValueAsString(table);
        objectMapper.readValue(serialized, GameTable.class);

        Assert.assertEquals(table.getSpecification(), specification);

        instantGameRequest.setPlayerId(2);
        GameTable<TicTacToeState> anotherTable = gameStateManager.construct(instantGameRequest);

        if (table.getTableId() != anotherTable.getTableId()) {
            instantGameRequest.setPlayerId(3);
            anotherTable = gameStateManager.construct(instantGameRequest);
        }

        Assert.assertEquals(table.getTableId(), anotherTable.getTableId());
    }
}
