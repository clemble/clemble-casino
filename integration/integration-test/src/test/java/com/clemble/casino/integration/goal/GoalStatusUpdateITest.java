package com.clemble.casino.integration.goal;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.goal.event.action.GoalStatusUpdateAction;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.utils.AsyncUtils;
import com.clemble.casino.lifecycle.initiation.InitiationState;
import com.clemble.test.concurrent.AsyncCompletionUtils;
import com.clemble.test.concurrent.Check;
import com.clemble.test.random.ObjectGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mavarazy on 1/23/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class GoalStatusUpdateITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void testUpdateStatus() {
        // Step 1. Creating player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 2. Creating GoalRequest
        GoalConfiguration CONFIGURATION = A.goalOperations().configurationService().getConfigurations().get(0);
        GoalConstructionRequest goalRequest = new GoalConstructionRequest(CONFIGURATION, "Status update test", "UTC");
        final GoalConstruction construction = A.goalOperations().constructionService().construct(goalRequest);
        final String goalKey = construction.getGoalKey();
        // Step 3. Checking construction
        AsyncCompletionUtils.check(new Check() {
            @Override
            public boolean check() {
                return A.goalOperations().initiationService().get(goalKey).getState() == InitiationState.initiated;
            }
        }, 30_000);
        final String newStatus = ObjectGenerator.generate(String.class);
        // Step 4. Checking goal started
        boolean goalStarted = AsyncUtils.checkNotNull((i) -> A.goalOperations().actionService().getState(goalKey));
        Assert.assertTrue(goalStarted);
        A.goalOperations().actionService().process(goalKey, new GoalStatusUpdateAction(newStatus));
        // Step 5. Checking goal
        boolean goalStatusUpdated = AsyncUtils.check((i) -> A.goalOperations().actionService().getState(goalKey).getStatus().equals(newStatus));
        Assert.assertTrue(goalStatusUpdated);
    }

}
