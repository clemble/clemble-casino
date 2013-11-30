package com.clemble.casino.server.game.construct;

import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.base.ActionLatch;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.AvailabilityGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameConstructionState;
import com.clemble.casino.game.construct.GameDeclineBehavior;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.game.SimpleGameSpringConfiguration;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = SpringConfiguration.UNIT_TEST)
@ContextConfiguration(classes = { SimpleGameSpringConfiguration.class })
public class GameConstructionRepositoryTest {

    @Autowired
    public GameConstructionRepository constructionRepository;

    @Autowired
    public ObjectMapper objectMapper;

    @Test
    public void testActionLatchSerialization() throws JsonParseException, JsonMappingException, IOException {
        String serializedLatch = null;
        try {
            ActionLatch randomLatch = new ActionLatch(ImmutableList.<String>of("1", "2"), "test");
            serializedLatch = objectMapper.writeValueAsString(randomLatch);
            ActionLatch readLatch = objectMapper.readValue(serializedLatch, ActionLatch.class);
            Assert.assertEquals("Failed to deserialize: " + serializedLatch, readLatch, randomLatch);
        } catch (JsonMappingException mappingException) {
            Assert.fail("Failed to instantiate: " + serializedLatch);
        }
    }

    @Test
    public void testSaving() {
        AvailabilityGameRequest availabilityGameRequest = new AvailabilityGameRequest("1", GameSpecification.DEFAULT, ImmutableList.<String> of("1", "2"), GameDeclineBehavior.invalidate);

        GameConstruction construction = new GameConstruction(availabilityGameRequest);
        construction.setSession(new GameSessionKey(Game.num, UUID.randomUUID().toString()));
        construction.setState(GameConstructionState.pending);
        construction.setResponses(new ActionLatch("1", "answer"));
        Assert.assertNotNull(construction.getResponses());
        construction = constructionRepository.saveAndFlush(construction);
        Assert.assertNotNull(construction.getResponses());
    }

    @Test
    public void testSaving2() {
        AvailabilityGameRequest availabilityGameRequest = new AvailabilityGameRequest("1", GameSpecification.DEFAULT, ImmutableList.<String> of("1", "2"), GameDeclineBehavior.invalidate);

        GameConstruction construction = new GameConstruction(availabilityGameRequest);
        construction.setState(GameConstructionState.pending);
        construction.setSession(new GameSessionKey(Game.num, UUID.randomUUID().toString()));
        construction.setResponses(new ActionLatch("1", "answer"));
        Assert.assertNotNull(construction.getResponses());
        construction = constructionRepository.saveAndFlush(construction);
        Assert.assertNotNull(construction.getResponses());

        GameConstruction anotherConstruction = new GameConstruction(availabilityGameRequest);
        anotherConstruction.setSession(new GameSessionKey(Game.num, UUID.randomUUID().toString()));
        anotherConstruction.setState(GameConstructionState.pending);
        anotherConstruction.setResponses(new ActionLatch("1", "answer"));
        Assert.assertNotNull(anotherConstruction.getResponses());
        anotherConstruction = constructionRepository.saveAndFlush(anotherConstruction);
        Assert.assertNotNull(anotherConstruction.getResponses());
        
        assertNotEquals(anotherConstruction.getSession(), construction.getSession());

    }
}
