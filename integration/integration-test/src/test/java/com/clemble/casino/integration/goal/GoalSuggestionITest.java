package com.clemble.casino.integration.goal;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.construction.GoalSuggestion;
import com.clemble.casino.goal.lifecycle.construction.GoalSuggestionRequest;
import com.clemble.casino.goal.lifecycle.construction.GoalSuggestionResponse;
import com.clemble.casino.goal.lifecycle.construction.GoalSuggestionState;
import com.clemble.casino.goal.lifecycle.construction.notification.GoalSuggestionNotification;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.utils.CheckUtils;
import com.clemble.casino.utils.CollectionUtils;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        GoalSuggestion suggestionAtoB = A.goalOperations().suggestionService().addSuggestion(B.getPlayer(), new GoalSuggestionRequest("Test goal A", "UTC"));
        // Step 3. Checking B have suggestion
        List<GoalSuggestion> suggestionAB = B.goalOperations().suggestionService().listMy();
        Assert.assertEquals(ImmutableList.of(suggestionAtoB), suggestionAB);
    }

//    @Test
//    public void testSuggested() {
//        // Step 1. Creating 2 players
//        ClembleCasinoOperations A = playerScenarios.createPlayer();
//        ClembleCasinoOperations B = playerScenarios.createPlayer();
//        // Step 2. Suggesting new goal for player A
//        Assert.assertEquals(A.goalOperations().suggestionService().listMy().size(), 0);
//        Assert.assertEquals(B.goalOperations().suggestionService().listMy().size(), 0);
//        GoalConfiguration configuration = A.goalOperations().configurationService().getConfigurations().get(0);
//        GoalSuggestion suggestionAtoB = A.goalOperations().suggestionService().addSuggestion(B.getPlayer(), new GoalSuggestionRequest("Test goal A", "UTC"));
//        // Step 3. Checking B have suggestion
//        List<GoalSuggestion> suggestionAB = B.goalOperations().suggestionService().listMy();
//        Assert.assertEquals(ImmutableList.of(suggestionAtoB), suggestionAB);
//        // Step 4. Checking A has suggestion in pending
//        List<GoalSuggestion> suggestedA = A.goalOperations().suggestionService().listMySuggested();
//        Assert.assertEquals(suggestionAB, suggestedA);
//    }

    @Test
    public void testApprovedInSuggested() {
        // Step 1. Creating 2 players
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        ClembleCasinoOperations B = playerScenarios.createPlayer();
        // Step 2. Suggesting new goal for player A
        Assert.assertEquals(A.goalOperations().suggestionService().listMy().size(), 0);
        Assert.assertEquals(B.goalOperations().suggestionService().listMy().size(), 0);
        GoalConfiguration configuration = A.goalOperations().configurationService().getConfigurations().get(0);
        GoalSuggestion suggestionAtoB = A.goalOperations().suggestionService().addSuggestion(B.getPlayer(), new GoalSuggestionRequest("Test goal A", "UTC"));
        // Step 3. Checking B have suggestion
        List<GoalSuggestion> suggestionAB = B.goalOperations().suggestionService().listMy();
        Assert.assertEquals(ImmutableList.of(suggestionAtoB), suggestionAB);
        // Step 4. Accepting suggestion
        B.goalOperations().suggestionService().reply(suggestionAtoB.getGoalKey(), new GoalSuggestionResponse(configuration, true));
        // Step 5. Checking suggestion in the list of suggestions
        Assert.assertTrue(CheckUtils.checkNotNull((i) -> CollectionUtils.find(GoalSuggestionNotification.class, A.notificationService().myNotifications())));
        GoalSuggestion suggestion = CollectionUtils.find(GoalSuggestionNotification.class, A.notificationService().myNotifications()).getSuggestion();
        Assert.assertEquals(GoalSuggestionState.accepted, suggestion.getState());
    }

    @Test
    public void testDeclinedInSuggested() {
        // Step 1. Creating 2 players
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        ClembleCasinoOperations B = playerScenarios.createPlayer();
        // Step 2. Suggesting new goal for player A
        Assert.assertEquals(A.goalOperations().suggestionService().listMy().size(), 0);
        Assert.assertEquals(B.goalOperations().suggestionService().listMy().size(), 0);
        GoalConfiguration configuration = A.goalOperations().configurationService().getConfigurations().get(0);
        GoalSuggestion suggestionAtoB = A.goalOperations().suggestionService().addSuggestion(B.getPlayer(), new GoalSuggestionRequest("Test goal A", "UTC"));
        // Step 3. Checking B have suggestion
        List<GoalSuggestion> suggestionAB = B.goalOperations().suggestionService().listMy();
        Assert.assertEquals(ImmutableList.of(suggestionAtoB), suggestionAB);
        // Step 4. Accepting suggestion
        B.goalOperations().suggestionService().reply(suggestionAtoB.getGoalKey(), new GoalSuggestionResponse(configuration, false));
        // Step 5. Checking suggestion in the list of suggestions
        Assert.assertTrue(CheckUtils.checkNotNull((i) -> CollectionUtils.find(GoalSuggestionNotification.class, A.notificationService().myNotifications())));
        GoalSuggestion suggestion = CollectionUtils.find(GoalSuggestionNotification.class, A.notificationService().myNotifications()).getSuggestion();
        Assert.assertEquals(GoalSuggestionState.declined, suggestion.getState());
    }

}
