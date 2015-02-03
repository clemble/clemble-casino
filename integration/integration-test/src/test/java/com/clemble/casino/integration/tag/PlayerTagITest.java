package com.clemble.casino.integration.tag;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.goal.GoalOperations;
import com.clemble.casino.goal.event.action.GoalReachedAction;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.utils.CheckUtils;
import com.clemble.casino.tag.ClembleTag;
import org.junit.Assert;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * Created by mavarazy on 2/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class PlayerTagITest {

    @Autowired
    public PlayerScenarios scenarios;

    @Test
    public void testTagAdded() {
        // Step 1. Creating player
        ClembleCasinoOperations A = scenarios.createPlayer();
        // Step 2. Scheduling task with tag
        GoalOperations Agoal = A.goalOperations();
        // Step 3. Creating new goal with a tag
        GoalConfiguration configuration = Agoal.configurationService().getConfigurations().get(0);
        GoalConstruction construction = Agoal.constructionService().construct(new GoalConstructionRequest(configuration, "#sport goal", DateTimeZone.UTC));
        // Step 4. Checking goal started
        Assert.assertTrue(CheckUtils.checkNotNull((i) -> Agoal.actionService().getState(construction.getGoalKey())));
        // Step 5. Sending goal reached event
        Agoal.actionService().process(construction.getGoalKey(), new GoalReachedAction("I'm done"));
        // Step 6. Checking tag added
        Assert.assertTrue(CheckUtils.check((i) -> {
            Set<ClembleTag> tags = A.tagService().myTags();
            if(tags.isEmpty())
                return false;
            ClembleTag tag = tags.iterator().next();
            return tag.getPower() == 1 && tag.getTag().equals("sport");
        }));
    }

}
