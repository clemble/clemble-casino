package com.clemble.casino.server.game.construct;

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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.AvailabilityGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameConstructionState;
import com.clemble.casino.game.event.schedule.InvitationAcceptedEvent;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.server.game.construct.GameConstructionServerService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.game.GameManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(SpringConfiguration.UNIT_TEST)
@ContextConfiguration(classes = { GameManagementSpringConfiguration.class })
public class GameConstructionServerServiceTest {

    final private static Random RANDOM = new Random();

    final private int NUM_PARTICIPANTS = 50;

    @Autowired
    public GameConstructionServerService constructionService;

    @Autowired
    public GameConstructionRepository constructionRepository;

    @Test
    public void testSynchronousAvailabilityConstruction() {
        LinkedHashSet<String> players = new LinkedHashSet<>();
        for (int i = 0; i < NUM_PARTICIPANTS; i++)
            players.add(String.valueOf(RANDOM.nextLong()));
        List<String> participants = new ArrayList<>(players);

        AvailabilityGameRequest availabilityGameRequest = new AvailabilityGameRequest(participants.get(0), GameSpecification.DEFAULT, participants);
        GameConstruction construction = constructionService.construct(availabilityGameRequest);

        for (int i = 1; i < NUM_PARTICIPANTS; i++) {
            constructionService.invitationResponsed(new InvitationAcceptedEvent(participants.get(i), construction.getSession()));
        }

        GameConstruction finalConstructionState = constructionRepository.findOne(construction.getSession());
        Assert.assertEquals(finalConstructionState.getState(), GameConstructionState.constructed);
    }

    @Test
    public void testAsynchronousAvailabilityConstruction() throws InterruptedException {
        LinkedHashSet<String> players = new LinkedHashSet<>();
        for (int i = 0; i < NUM_PARTICIPANTS; i++)
            players.add(String.valueOf(RANDOM.nextLong()));
        List<String> participants = new ArrayList<>(players);

        AvailabilityGameRequest availabilityGameRequest = new AvailabilityGameRequest(participants.get(0), GameSpecification.DEFAULT, participants);
        GameConstruction construction = constructionService.construct(availabilityGameRequest);

        final CountDownLatch downLatch = new CountDownLatch(NUM_PARTICIPANTS - 1);
        Collection<Callable<GameConstruction>> constructionJobs = new ArrayList<>();
        for (int i = 1; i < NUM_PARTICIPANTS; i++) {
            constructionJobs.add(new GameResponce(construction.getSession(), participants.get(i), downLatch, constructionService));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_PARTICIPANTS);
        executorService.invokeAll(constructionJobs);

        downLatch.await(10, TimeUnit.SECONDS);

        GameConstruction finalConstructionState = constructionRepository.findOne(construction.getSession());
        Assert.assertEquals(downLatch.getCount(), 0);
        Assert.assertEquals(finalConstructionState.getState(), GameConstructionState.constructed);
    }

    public static class GameResponce implements Callable<GameConstruction> {

        final public String player;
        final public GameSessionKey construction;
        final public CountDownLatch endLatch;
        final public GameConstructionServerService constructionService;

        public GameResponce(GameSessionKey construction, String player, CountDownLatch endLatch, GameConstructionServerService constructionService) {
            this.player = player;
            this.construction = construction;
            this.endLatch = endLatch;
            this.constructionService = constructionService;
        }

        @Override
        public GameConstruction call() {
            try {
                Thread.sleep(RANDOM.nextInt(1000));
                GameConstruction construct = constructionService.invitationResponsed(new InvitationAcceptedEvent(player, construction));
                System.out.println(construct.getVersion());
                return construct;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                call();
            } finally {
                endLatch.countDown();
            }
            return null;
        }

    }
}
