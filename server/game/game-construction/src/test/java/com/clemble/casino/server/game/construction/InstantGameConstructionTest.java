package com.clemble.casino.server.game.construction;

import com.clemble.casino.construction.ConstructionState;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.server.game.construction.controller.AutoGameConstructionController;
import com.clemble.casino.server.game.construction.repository.GameConstructionRepository;
import com.clemble.casino.server.game.construction.spring.GameConstructionSpringConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.game.construction.AutomaticGameRequest;
import com.clemble.casino.game.construction.GameConstruction;
import com.clemble.casino.server.spring.common.SpringConfiguration;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = SpringConfiguration.TEST)
@ContextConfiguration(classes = { GameConstructionSpringConfiguration.class })
public class InstantGameConstructionTest {

    @Autowired
    public AutoGameConstructionController constructionService;

    @Autowired
    public GameConstructionRepository constructionRepository;

    @Test
    public void testInstantConstruction() {
        // Step 1. Generating random game
        RoundGameConfiguration specification = RoundGameConfiguration.DEFAULT;
        // Step 2. Generating player A
        String A = RandomStringUtils.random(10);
        GameConstruction constructionA = constructionService.construct(new AutomaticGameRequest(A, specification));
        // Step 3. Generating player B
        String B = RandomStringUtils.random(10);
        GameConstruction constructionB = constructionService.construct(new AutomaticGameRequest(B, specification));
        // Step 4. Checking all of them matched
        Assert.assertEquals(constructionRepository.findOne(constructionA.getSessionKey()).getState(), ConstructionState.constructed);
        Assert.assertEquals(constructionRepository.findOne(constructionB.getSessionKey()).getState(), ConstructionState.constructed);
    }

}
