package com.clemble.casino.integration.goal;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.construction.GoalSuggestion;
import com.clemble.casino.goal.lifecycle.construction.GoalSuggestionRequest;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by mavarazy on 1/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class GoalSuggestionITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void testSuggestion() {
        // Step 1. Creating 2 players
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        ClembleCasinoOperations B = playerScenarios.createPlayer();
        // Step 2. Suggesting new goal for player A
        Assert.assertEquals(A.goalOperations().suggestionService().listMy().size(), 0);
        Assert.assertEquals(B.goalOperations().suggestionService().listMy().size(), 0);
        GoalConfiguration configuration = A.goalOperations().configurationService().getConfigurations().get(0);
        GoalSuggestion suggestionAtoB = A.goalOperations().suggestionService().addSuggestion(B.getPlayer(), new GoalSuggestionRequest("Test goal A", configuration));
        // Step 3. Checking B have suggestion
        List<GoalSuggestion> suggestionAB = B.goalOperations().suggestionService().listMy();
        Assert.assertEquals(ImmutableList.of(suggestionAtoB), suggestionAB);
    }

}
