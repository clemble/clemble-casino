package com.clemble.casino.integration.feed;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.goal.GoalOperations;
import com.clemble.casino.goal.event.action.GoalReachedAction;
import com.clemble.casino.goal.event.action.GoalStatusUpdateAction;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.goal.lifecycle.management.GoalRole;
import com.clemble.casino.goal.post.*;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.integration.utils.CheckUtils;
import com.clemble.casino.lifecycle.configuration.rule.bet.MonoBidRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

/**
 * Created by mavarazy on 12/1/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class GoalFeedTest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void testCreatedPost() {
        // Step 1. Creating player A
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final GoalOperations goalA = A.goalOperations();
        // Step 2. Checking configuration
        final GoalConfiguration configuration = (GoalConfiguration) goalA.configurationService().getConfigurations().get(0);
        // Step 3. Creating construction
        final GoalConstruction constructionA = goalA.constructionService().construct(new GoalConstructionRequest(configuration, "Goal A", new Date()));
        // Step 4. Checking post appeared in player feed
        CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalCreatedPost);
    }

    @Test
    public void testBidPost() {
        // Step 1. Creating player A
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final ClembleCasinoOperations B = playerScenarios.createPlayer();
        final GoalOperations goalA = A.goalOperations();
        final GoalOperations goalB = B.goalOperations();
        // Step 2. Checking configuration
        final GoalConfiguration configuration = (GoalConfiguration) goalA.configurationService().getConfigurations().get(1);
        // Step 3. Creating construction
        final GoalConstruction constructionA = goalA.constructionService().construct(new GoalConstructionRequest(configuration, "Goal A", new Date()));
        // Step 4. Checking post appeared in player feed
        CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalCreatedPost);
        // Step 5. Bidding on goal appeared
        goalB.initiationService().bid(constructionA.getGoalKey(), GoalRole.supporter);
        // Step 6. Checking bid appeared
        CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalBidPost);
    }

    @Test
    public void testStartedPost() {
        // Step 1. Creating player A
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final GoalOperations goalA = A.goalOperations();
        // Step 2. Checking configuration
        final GoalConfiguration configuration = (GoalConfiguration) goalA.configurationService().getConfigurations().get(0);
        // Step 3. Creating construction
        final GoalConstruction constructionA = goalA.constructionService().construct(new GoalConstructionRequest(configuration, "Goal A", new Date()));
        // Step 4. Checking post appeared in player feed
        CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalCreatedPost);
        // Step 5. Constructing
        goalA.initiationService().confirm(constructionA.getGoalKey());
        // Step 6. Checking started event
        CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalStartedPost);
    }

    @Test
    public void testUpdatedPost() {
        // Step 1. Creating player A
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final GoalOperations goalA = A.goalOperations();
        // Step 2. Checking configuration
        final GoalConfiguration configuration = (GoalConfiguration) goalA.configurationService().getConfigurations().get(0);
        // Step 3. Creating construction
        final GoalConstruction constructionA = goalA.constructionService().construct(new GoalConstructionRequest(configuration, "Goal A", new Date()));
        // Step 4. Checking post appeared in player feed
        CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalCreatedPost);
        // Step 5. Constructing
        goalA.initiationService().confirm(constructionA.getGoalKey());
        // Step 6. Checking started event
        CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalStartedPost);
        // Step 7. Updating status
        goalA.actionService().process(constructionA.getGoalKey(), new GoalStatusUpdateAction("Update action"));
        // Step 8. Checking post
        CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalUpdatedPost);
    }

    @Test
    public void testReachedPost() {
        // Step 1. Creating player A
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final GoalOperations goalA = A.goalOperations();
        // Step 2. Checking configuration
        final GoalConfiguration configuration = (GoalConfiguration) goalA.configurationService().getConfigurations().get(0);
        // Step 3. Creating construction
        final GoalConstruction constructionA = goalA.constructionService().construct(new GoalConstructionRequest(configuration, "Goal A", new Date()));
        // Step 4. Checking post appeared in player feed
        CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalCreatedPost);
        // Step 5. Constructing
        goalA.initiationService().confirm(constructionA.getGoalKey());
        // Step 6. Checking started event
        CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalStartedPost);
        // Step 7. Updating status
        goalA.actionService().process(constructionA.getGoalKey(), new GoalReachedAction("Update action"));
        // Step 8. Checking post
        CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalReachedPost);
    }

}

