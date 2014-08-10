package com.clemble.casino.server.game;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.clemble.casino.game.specification.RoundGameConfiguration;
import com.clemble.casino.server.game.pending.PendingGameInitiation;
import com.clemble.casino.server.game.pending.PendingPlayer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.repository.game.PendingGameInitiationRepository;
import com.clemble.casino.server.repository.game.PendingPlayerRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.game.SimpleGameSpringConfiguration;
import com.google.common.collect.ImmutableList;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SimpleGameSpringConfiguration.class })
public class PendingGameTest {

    @Autowired
    public PendingPlayerRepository playerRepository;

    @Autowired
    public PendingGameInitiationRepository initiationRepository;

    @Test
    public void testCreation(){
        // Step 1. Creating simple player
        PendingPlayer A = new PendingPlayer("A");
        playerRepository.save(A);
        PendingPlayer B = new PendingPlayer("B");
        playerRepository.save(B);
        // Step 2. Creating and saving game initiation
        GameInitiation initiation = new GameInitiation(new GameSessionKey(Game.pic, "AB"), ImmutableList.<String>of("A", "B"), RoundGameConfiguration.DEFAULT);
        PendingGameInitiation pendingGameInitiation = new PendingGameInitiation(initiation);
        pendingGameInitiation = initiationRepository.save(pendingGameInitiation);
        // Step 3. Checking values
        assertTrue(playerRepository.findPending("A").iterator().hasNext());
        assertTrue(playerRepository.findPending("B").iterator().hasNext());
        // Step 4. Checking was able to read values
        List<PendingGameInitiation> initiations = initiationRepository.findPending("A");
        PendingGameInitiation pendingInitiation = initiations.get(0);
        assertNotNull(pendingInitiation);
        assertNotNull(pendingInitiation.getSession());
        assertNotNull(pendingInitiation.getConfigurationKey());
    }

}
