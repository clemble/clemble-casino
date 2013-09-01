package com.gogomaya.server.game.construct;

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

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.game.construct.AvailabilityGameRequest;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameConstructionState;
import com.gogomaya.game.event.schedule.InvitationAcceptedEvent;
import com.gogomaya.game.specification.GameSpecification;
import com.gogomaya.server.repository.game.GameConstructionRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.game.GameManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(SpringConfiguration.UNIT_TEST)
@ContextConfiguration(classes = { GameManagementSpringConfiguration.class })
public class GameConstructionProcessingServiceTest {

    final private Random RANDOM = new Random();

    final private int NUM_PARTICIPANTS = 50;

    @Inject
    public GameConstructionProcessingService constructionService;

    @Inject
    public GameConstructionRepository constructionRepository;

    @Test
    public void testSynchronousAvailabilityConstruction() {
        LinkedHashSet<Long> players = new LinkedHashSet<>();
        for (int i = 0; i < NUM_PARTICIPANTS; i++)
            players.add(RANDOM.nextLong());
        List<Long> participants = new ArrayList<>(players);

        AvailabilityGameRequest availabilityGameRequest = new AvailabilityGameRequest(participants.get(0), GameSpecification.DEFAULT, participants);
        GameConstruction construction = constructionService.construct(availabilityGameRequest);

        for (int i = 1; i < NUM_PARTICIPANTS; i++) {
            constructionService.invitationResponsed(new InvitationAcceptedEvent(construction.getSession(), participants.get(i)));
        }

        GameConstruction finalConstructionState = constructionRepository.findOne(construction.getSession());
        Assert.assertEquals(finalConstructionState.getState(), GameConstructionState.constructed);
    }

    @Test
    public void testAsynchronousAvailabilityConstruction() throws InterruptedException {
        LinkedHashSet<Long> players = new LinkedHashSet<>();
        for (int i = 0; i < NUM_PARTICIPANTS; i++)
            players.add(RANDOM.nextLong());
        List<Long> participants = new ArrayList<>(players);

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

        final public CountDownLatch endLatch;
        final public GameConstructionProcessingService constructionService;
        final public long construction;
        final public long player;

        public GameResponce(long construction, long player, CountDownLatch endLatch, GameConstructionProcessingService constructionService) {
            this.construction = construction;
            this.player = player;
            this.endLatch = endLatch;
            this.constructionService = constructionService;
        }

        @Override
        public GameConstruction call() {
            try {
                GameConstruction construct = constructionService.invitationResponsed(new InvitationAcceptedEvent(construction, player));
                System.out.println(construct.getVersion());
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
