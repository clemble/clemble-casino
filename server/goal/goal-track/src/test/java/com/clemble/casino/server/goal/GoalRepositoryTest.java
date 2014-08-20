package com.clemble.casino.server.goal;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.GoalState;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.server.goal.repository.GoalRepository;
import com.clemble.casino.server.goal.spring.GoalSpringConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by mavarazy on 8/11/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GoalSpringConfiguration.class)
public class GoalRepositoryTest {

    @Autowired
    public GoalRepository goalRepository;

    @Test
    public void testFindByPatient(){
        Bid bid = new Bid(Money.create(Currency.DEFAULT, 50), Money.create(Currency.DEFAULT, 60));
        String player = RandomStringUtils.random(5);
        Goal ga = new Goal(RandomStringUtils.random(5), player, player, "A", null, new Date(System.currentTimeMillis()), GoalState.pending, null, bid);
        goalRepository.save(ga);
        Assert.assertEquals(goalRepository.findByPlayer(player).iterator().next(), ga);
    }

}
