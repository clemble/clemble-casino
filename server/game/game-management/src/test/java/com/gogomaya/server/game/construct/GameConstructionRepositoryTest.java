package com.gogomaya.server.game.construct;

import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.base.ActionLatch;
import com.gogomaya.game.construct.AvailabilityGameRequest;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameConstructionState;
import com.gogomaya.game.construct.GameDeclineBehavior;
import com.gogomaya.game.specification.GameSpecification;
import com.gogomaya.server.repository.game.GameConstructionRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.game.GameManagementSpringConfiguration;
import com.google.common.collect.ImmutableList;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = SpringConfiguration.UNIT_TEST)
@ContextConfiguration(classes = { GameManagementSpringConfiguration.class })
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
        construction.setState(GameConstructionState.pending);
        Assert.assertNotNull(construction.getResponses());
        construction = constructionRepository.saveAndFlush(construction);
        Assert.assertNotNull(construction.getResponses());
    }

    @Test
    public void testSaving2() {
        AvailabilityGameRequest availabilityGameRequest = new AvailabilityGameRequest("1", GameSpecification.DEFAULT, ImmutableList.<String> of("1", "2"), GameDeclineBehavior.invalidate);

        GameConstruction construction = new GameConstruction(availabilityGameRequest);
        construction.setState(GameConstructionState.pending);
        Assert.assertNotNull(construction.getResponses());
        construction = constructionRepository.saveAndFlush(construction);
        Assert.assertNotNull(construction.getResponses());

        GameConstruction anotherConstruction = new GameConstruction(availabilityGameRequest);
        anotherConstruction.setState(GameConstructionState.pending);
        Assert.assertNotNull(anotherConstruction.getResponses());
        anotherConstruction = constructionRepository.saveAndFlush(anotherConstruction);
        Assert.assertNotNull(anotherConstruction.getResponses());
        
        assertNotEquals(anotherConstruction.getSession(), construction.getSession());

    }
}
