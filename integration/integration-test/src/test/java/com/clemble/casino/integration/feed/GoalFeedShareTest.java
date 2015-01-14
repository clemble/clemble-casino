package com.clemble.casino.integration.feed;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.event.PlayerEventSelector;
import com.clemble.casino.client.goal.GoalOperations;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.goal.post.GoalCreatedPost;
import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.integration.utils.CheckUtils;
import com.clemble.casino.post.PlayerPost;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.event.share.SystemSharePostEvent;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 1/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class GoalFeedShareTest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Autowired
    public EventAccumulator<SystemEvent> systemEventAccumulator;

    @Test
    public void sharePost() {
        // Step 1. Creating player A
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final GoalOperations goalA = A.goalOperations();
        // Step 2. Checking configuration
        final GoalConfiguration configuration = (GoalConfiguration) goalA.configurationService().getConfigurations().get(0);
        // Step 3. Creating construction
        final GoalConstruction constructionA = goalA.constructionService().construct(new GoalConstructionRequest(configuration, "Goal A", new DateTime(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2))));
        // Step 4. Checking post appeared in player feed
        CheckUtils.check((i) -> A.feedService().myFeed().length == 1 && A.feedService().myFeed()[0] instanceof GoalCreatedPost);
        // Step 5. Checking share works
        GoalCreatedPost post = (GoalCreatedPost) A.feedService().myFeed()[0];
        Assert.assertNotNull(post);
        // Step 6. Checking share works
        A.feedService().share(post.getKey(), "facebook");
        // Step 7. Checking notification was send
        EventSelector postSelector = EventSelectors.where(new EventTypeSelector(SystemSharePostEvent.class)).and(new PlayerEventSelector(A.getPlayer()));
        SystemSharePostEvent shareEvent = systemEventAccumulator.waitFor(postSelector);
        Assert.assertEquals(shareEvent.getPlayer(), A.getPlayer());
        Assert.assertEquals(shareEvent.getProviderId(), "facebook");
        Assert.assertEquals(shareEvent.getPost(), post);
    }

}
