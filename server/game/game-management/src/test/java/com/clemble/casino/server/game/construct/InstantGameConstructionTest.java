package com.clemble.casino.server.game.construct;

import com.clemble.casino.game.specification.RoundGameConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.game.construct.AutomaticGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameConstructionState;
import com.clemble.casino.game.service.AutoGameConstructionService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.game.SimpleGameSpringConfiguration;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = SpringConfiguration.TEST)
@ContextConfiguration(classes = { SimpleGameSpringConfiguration.class })
public class InstantGameConstructionTest {

    @Autowired
    public AutoGameConstructionService constructionService;

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
        Assert.assertEquals(constructionRepository.findOne(constructionA.getSessionKey()).getState(), GameConstructionState.constructed);
        Assert.assertEquals(constructionRepository.findOne(constructionB.getSessionKey()).getState(), GameConstructionState.constructed);
    }

}
