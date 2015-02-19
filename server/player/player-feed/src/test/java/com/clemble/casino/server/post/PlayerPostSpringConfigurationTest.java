package com.clemble.casino.server.post;

import com.clemble.casino.bet.Bet;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalContext;
import com.clemble.casino.goal.lifecycle.management.GoalPhase;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.post.GoalStartedPost;
import com.clemble.casino.lifecycle.management.event.action.Action;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.payment.Bank;
import com.clemble.casino.post.PlayerPost;
import com.clemble.casino.server.post.repository.PlayerPostRepository;
import com.clemble.casino.server.post.spring.PlayerFeedSpringConfiguration;
import com.clemble.test.random.ObjectGenerator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

/**
 * Created by mavarazy on 11/30/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PlayerFeedSpringConfiguration.class)
public class PlayerPostSpringConfigurationTest {

    @Autowired
    public PlayerPostRepository postRepository;

    @Test
    public void test() {
    }

    @Test
    public void savePost() {
        GoalState state = new GoalState(
            ObjectGenerator.generate(String.class),
            ObjectGenerator.generate(DateTime.class),
            ObjectGenerator.generate(DateTime.class),
            ObjectGenerator.generate(String.class),
            ObjectGenerator.generate(Bank.class),
            ObjectGenerator.generate(String.class),
            DateTimeZone.UTC,
            ObjectGenerator.generate(String.class),
            ObjectGenerator.generate(GoalConfiguration.class),
            new GoalContext(null, Collections.emptyList()),
            Collections.<String>singleton(ObjectGenerator.generate(String.class)),
            ObjectGenerator.generate(String.class),
            ObjectGenerator.generate(GoalPhase.class),
            ObjectGenerator.generate(Action.class)
        );
        // Step 1. Creating post
        GoalStartedPost post = new GoalStartedPost(
            "A",
            "B",
            state,
            DateTime.now(DateTimeZone.UTC)
        );
        // Step 2. Saving player post twice
        postRepository.save(post);
        postRepository.save(post);
        postRepository.save(post);
        // Step 3. Checking fetch works
        List<PlayerPost> posts = postRepository.findByPlayerInOrderByCreatedDesc(Collections.singleton("B"));
        Assert.assertEquals(posts.size(), 1);
        Assert.assertEquals(posts.get(0), post);
    }

}
