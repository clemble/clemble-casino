package com.gogomaya.server.game.table;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.tictactoe.PicPacPoeSpringConfiguration;
import com.gogomaya.server.test.RedisCleaner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(SpringConfiguration.PROFILE_TEST)
@ContextConfiguration(classes = { PicPacPoeSpringConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class GameTableManagerTest {
    
    @Test
    public void test(){
    }
    
/** TODO
    @Inject
    GameConstructionServiceFacade<TicTacToeState> gameStateManager;

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
    */
}
