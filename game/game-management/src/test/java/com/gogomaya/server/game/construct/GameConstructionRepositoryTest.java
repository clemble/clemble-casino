package com.gogomaya.server.game.construct;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.spring.game.GameManagementSpringConfiguration;
import com.google.common.collect.ImmutableList;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { GameManagementSpringConfiguration.class })
public class GameConstructionRepositoryTest {

    @Inject
    GameConstructionRepository constructionRepository;

    @Inject
    ObjectMapper objectMapper;

    @Test
    public void testActionLatchSerialization() throws JsonParseException, JsonMappingException, IOException {
        String serializedLatch = null;
        try {
            ActionLatch randomLatch = new ActionLatch(ImmutableList.<Long>of(1L, 2L), "test");
            serializedLatch = objectMapper.writeValueAsString(randomLatch);
            ActionLatch readLatch = objectMapper.readValue(serializedLatch, ActionLatch.class);
            Assert.assertEquals("Failed to deserialize: " + serializedLatch, readLatch, randomLatch);
        } catch (JsonMappingException mappingException) {
            Assert.fail("Failed to instantiate: " + serializedLatch);
        }
    }

    @Test
    public void testSaving() {
        AvailabilityGameRequest availabilityGameRequest = new AvailabilityGameRequest(1L, GameSpecification.DEFAULT, ImmutableList.<Long> of(1L, 2L), GameDeclineBehavior.invalidate);

        GameConstruction construction = new GameConstruction(availabilityGameRequest);
        Assert.assertNotNull(construction.getResponces());
        construction = constructionRepository.saveAndFlush(construction);
        Assert.assertNotNull(construction.getResponces());
    }
}
