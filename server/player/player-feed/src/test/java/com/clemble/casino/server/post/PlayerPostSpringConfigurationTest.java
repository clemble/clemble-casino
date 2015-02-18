package com.clemble.casino.server.post;

import com.clemble.casino.bet.Bet;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalPhase;
import com.clemble.casino.goal.post.GoalStartedPost;
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
        // Step 1. Creating post
        GoalStartedPost post = new GoalStartedPost(
            "A",
            "AB",
            "B",
            new Bank(Collections.emptyList(), new Bet(Money.create(Currency.FakeMoney, 0), Money.create(Currency.FakeMoney, 0))),
            ObjectGenerator.generate(GoalConfiguration.class),
            "",
            new DateTime(0),
            Collections.emptySet(),
            DateTime.now(DateTimeZone.UTC),
            GoalPhase.started
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
