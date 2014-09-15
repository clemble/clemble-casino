package com.clemble.casino.server.game.construction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.clemble.casino.construction.ConstructionState;
import com.clemble.casino.game.construction.GameDeclineBehavior;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.server.game.construction.controller.AvailabilityGameConstructionController;
import com.clemble.casino.server.game.construction.repository.GameConstructionRepository;
import com.clemble.casino.server.game.construction.service.ServerAvailabilityGameConstructionService;
import com.clemble.casino.server.game.construction.spring.GameConstructionSpringConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.game.construction.AvailabilityGameRequest;
import com.clemble.casino.game.construction.GameConstruction;
import com.clemble.casino.game.construction.event.InvitationAcceptedEvent;
import com.clemble.casino.game.construction.service.AvailabilityGameConstructionService;
import com.clemble.casino.server.spring.common.SpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(SpringConfiguration.TEST)
@ContextConfiguration(classes = { GameConstructionSpringConfiguration.class })
public class ServerGameConstructionServiceTest {

    final private static Random RANDOM = new Random();

    final private int NUM_PARTICIPANTS = 50;

    @Autowired
    public ServerAvailabilityGameConstructionService constructionService;

    @Autowired
    public GameConstructionRepository constructionRepository;

    @Test
    public void testSynchronousAvailabilityConstruction() {
        LinkedHashSet<String> players = new LinkedHashSet<>();
        for (int i = 0; i < NUM_PARTICIPANTS; i++)
            players.add(String.valueOf(RANDOM.nextLong()));
        List<String> participants = new ArrayList<>(players);
        RoundGameConfiguration specification = generate(participants);

        AvailabilityGameRequest availabilityGameRequest = new AvailabilityGameRequest(specification, participants, GameDeclineBehavior.invalidate);
        GameConstruction construction = constructionService.construct(participants.get(0), availabilityGameRequest);

        for (int i = 1; i < NUM_PARTICIPANTS; i++) {
            constructionService.reply(new InvitationAcceptedEvent(participants.get(i), construction.getSessionKey()));
        }

        GameConstruction finalConstructionState = constructionRepository.findOne(construction.getSessionKey());
        Assert.assertEquals(finalConstructionState.getState(), ConstructionState.constructed);
    }

    @Test
    public void testAsynchronousAvailabilityConstruction() throws InterruptedException {
        LinkedHashSet<String> players = new LinkedHashSet<>();
        for (int i = 0; i < NUM_PARTICIPANTS; i++)
            players.add(RandomStringUtils.randomAlphanumeric(10));
        List<String> participants = new ArrayList<>(players);
        RoundGameConfiguration specification = generate(participants);

        AvailabilityGameRequest availabilityGameRequest = new AvailabilityGameRequest(specification, participants, GameDeclineBehavior.invalidate);
        GameConstruction construction = constructionService.construct(participants.get(0), availabilityGameRequest);

        final CountDownLatch downLatch = new CountDownLatch(NUM_PARTICIPANTS - 1);
        Collection<Callable<GameConstruction>> constructionJobs = new ArrayList<>();
        for (int i = 1; i < NUM_PARTICIPANTS; i++) {
            constructionJobs.add(new GameResponce(construction.getSessionKey(), participants.get(i), downLatch, constructionService));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_PARTICIPANTS);
        executorService.invokeAll(constructionJobs);

        downLatch.await(10, TimeUnit.SECONDS);

        GameConstruction finalConstructionState = constructionRepository.findOne(construction.getSessionKey());
        Assert.assertEquals(downLatch.getCount(), 0);
        Assert.assertEquals(finalConstructionState.getState(), ConstructionState.constructed);
    }

    private RoundGameConfiguration generate(List<String> roles) {
        return RoundGameConfiguration.DEFAULT;
    }

    public static class GameResponce implements Callable<GameConstruction> {

        final public String player;
        final public String sessionKey;
        final public CountDownLatch endLatch;
        final public AvailabilityGameConstructionService constructionService;

        public GameResponce(String sessionKey, String player, CountDownLatch endLatch, AvailabilityGameConstructionService constructionService) {
            this.player = player;
            this.sessionKey = sessionKey;
            this.endLatch = endLatch;
            this.constructionService = constructionService;
        }

        @Override
        public GameConstruction call() {
            try {
                Thread.sleep(RANDOM.nextInt(1000));
                GameConstruction construct = constructionService.reply(new InvitationAcceptedEvent(player, sessionKey));
                return construct;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                endLatch.countDown();
            }
            return null;
        }

    }
}
