package com.clemble.casino.integration.feed;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.goal.GoalOperations;
import com.clemble.casino.goal.event.action.GoalReachedAction;
import com.clemble.casino.goal.event.action.GoalStatusUpdateAction;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.GoalRoleConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.NoReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.share.ShareRule;
import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.goal.lifecycle.management.GoalRole;
import com.clemble.casino.goal.post.*;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.integration.utils.CheckUtils;
import com.clemble.casino.lifecycle.configuration.rule.breach.LooseBreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.privacy.PrivacyRule;
import com.clemble.casino.lifecycle.configuration.rule.timeout.MoveTimeoutCalculator;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TimeoutRule;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TotalTimeoutCalculator;
import com.clemble.casino.lifecycle.management.event.action.bet.BidAction;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.player.service.PlayerFeedService;
import com.clemble.casino.post.PlayerPost;
import org.junit.Assert;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.TimeUnit;

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
    public void testStartedPost() {
        // Step 1. Creating player A
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final GoalOperations goalA = A.goalOperations();
        // Step 2. Checking configuration
        final GoalConfiguration configuration = (GoalConfiguration) goalA.configurationService().getConfigurations().get(0);
        // Step 3. Creating construction
        final GoalConstruction constructionA = goalA.constructionService().construct(new GoalConstructionRequest(configuration, "Goal A", DateTimeZone.UTC));
        // Step 4. Checking post appeared in player feed
        Assert.assertTrue(CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalStartedPost));
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
        final GoalConstruction constructionA = goalA.constructionService().construct(new GoalConstructionRequest(configuration, "Goal A", DateTimeZone.UTC));
        // Step 4. Checking post appeared in player feed
        Assert.assertTrue(CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalStartedPost));
        // Step 5. Bidding on goal appeared
        goalB.actionService().process(constructionA.getGoalKey(), new BidAction());
        // Step 6. Checking bid appeared
        Assert.assertTrue(CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalBetPost));
    }

//    @Test
//    public void testStartedPost() {
//        // Step 1. Creating player A
//        final ClembleCasinoOperations A = playerScenarios.createPlayer();
//        final PlayerFeedService feedA = A.feedService();
//        final GoalOperations goalA = A.goalOperations();
//        // Step 2. Checking configuration
//        final GoalConfiguration configuration = (GoalConfiguration) goalA.configurationService().getConfigurations().get(0);
//        // Step 3. Creating construction
//        final GoalConstruction constructionA = goalA.constructionService().construct(new GoalConstructionRequest(configuration, "Goal A", DateTimeZone.UTC));
//        // Step 4. Checking post appeared in player feed
//        Assert.assertTrue(CheckUtils.check((i) -> {
//            PlayerPost[] posts = feedA.myFeed();
//            return posts.length == 1 && posts[0] instanceof GoalStartedPost;
//        }));
//        // Step 5. Constructing
//        goalA.initiationService().confirm(constructionA.getGoalKey());
//        // Step 6. Checking started event
//        Assert.assertTrue(CheckUtils.check((i) -> {
//            PlayerPost[] posts = feedA.myFeed();
//            return posts.length == 1 && posts[0] instanceof GoalStartedPost;
//        }));
//    }

    @Test
    public void testUpdatedPost() {
        // Step 1. Creating player A
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final GoalOperations goalA = A.goalOperations();
        // Step 2. Checking configuration
        final GoalConfiguration configuration = (GoalConfiguration) goalA.configurationService().getConfigurations().get(0);
        // Step 3. Creating construction
        final GoalConstruction constructionA = goalA.constructionService().construct(new GoalConstructionRequest(configuration, "Goal A", DateTimeZone.UTC));
        // Step 4. Checking started event
        Assert.assertTrue(CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalStartedPost));
        // Step 5. Updating status
        goalA.actionService().process(constructionA.getGoalKey(), new GoalStatusUpdateAction("Update action"));
        // Step 6. Checking post
        Assert.assertTrue(CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalUpdatedPost));
    }

    @Test
    public void testReachedPost() {
        // Step 1. Creating player A
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final GoalOperations goalA = A.goalOperations();
        // Step 2. Checking configuration
        final GoalConfiguration configuration = (GoalConfiguration) goalA.configurationService().getConfigurations().get(0);
        // Step 3. Creating construction
        final GoalConstruction constructionA = goalA.constructionService().construct(new GoalConstructionRequest(configuration, "Goal A", DateTimeZone.UTC));
        // Step 4. Checking started event
        Assert.assertTrue(CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalStartedPost));
        // Step 5. Updating status
        goalA.actionService().process(constructionA.getGoalKey(), new GoalReachedAction("Update action"));
        // Step 6. Checking post
        Assert.assertTrue(CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalReachedPost));
    }

    final private GoalConfiguration FORBID_CONFIGURATION = new GoalConfiguration(
        "forbid",
        "Forbid Test",
        new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 50)),
        NoReminderRule.INSTANCE,
        NoReminderRule.INSTANCE,
        new TimeoutRule(LooseBreachPunishment.getInstance(), new MoveTimeoutCalculator(TimeUnit.SECONDS.toMillis(2))),
        new TimeoutRule(LooseBreachPunishment.getInstance(), new TotalTimeoutCalculator(TimeUnit.HOURS.toMillis(3))),
        PrivacyRule.world,
        new GoalRoleConfiguration(
            new Bid(Money.create(Currency.FakeMoney, 100), Money.create(Currency.FakeMoney, 50)),
            0,
            NoReminderRule.INSTANCE,
            NoReminderRule.INSTANCE
        ),
        ShareRule.none
    );

    @Test
    public void testForbidPost() {
        // Step 1. Creating player A
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final GoalOperations goalA = A.goalOperations();
        // Step 3. Creating construction
        goalA.constructionService().construct(new GoalConstructionRequest(FORBID_CONFIGURATION, "Goal A", DateTimeZone.UTC));
        // Step 4. Checking forbid post
        Assert.assertTrue(CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalBetOffPost));
    }

}

