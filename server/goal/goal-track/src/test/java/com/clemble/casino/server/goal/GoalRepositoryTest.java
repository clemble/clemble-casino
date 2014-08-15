package com.clemble.casino.server.goal;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.GoalKey;
import com.clemble.casino.goal.GoalState;
import com.clemble.casino.goal.GoalStatus;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.server.goal.repository.GoalRepository;
import com.clemble.casino.server.goal.spring.GoalSpringConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

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
        Bid bid = new Bid("A", "A", Money.create(Currency.DEFAULT, 50));
        Goal ga = new Goal(new GoalKey("A", "A"), "A", "A", new Date(System.currentTimeMillis()), GoalState.pending, new TreeSet<GoalStatus>());
        goalRepository.save(ga);
        Assert.assertEquals(goalRepository.findByPlayer("A").iterator().next(), ga);
    }

}
