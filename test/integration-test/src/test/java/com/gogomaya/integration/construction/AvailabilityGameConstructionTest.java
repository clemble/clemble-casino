package com.gogomaya.integration.construction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.game.construct.AvailabilityGameRequest;
import com.gogomaya.server.game.construct.GameDeclineBehavior;
import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.google.common.collect.ImmutableList;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(classes = { TestConfiguration.class })
public class AvailabilityGameConstructionTest {

    @Autowired
    PlayerOperations playerOperations;

    @Autowired
    GameOperations<?> gameOperations;

    @Test
    public void testSimpleCreation() {
        Player playerA = playerOperations.createPlayer();
        Player playerB = playerOperations.createPlayer();

        AvailabilityGameRequest gameRequest = new AvailabilityGameRequest();
        gameRequest.setPlayerId(playerA.getPlayerId());
        gameRequest.setParticipants(ImmutableList.of(playerA.getPlayerId(), playerB.getPlayerId()));
        gameRequest.setDeclineBehavior(GameDeclineBehavior.invalidate);
        gameRequest.setSpecification(gameOperations.selectSpecification());

        gameOperations.construct(playerA, gameRequest);
    }
}
