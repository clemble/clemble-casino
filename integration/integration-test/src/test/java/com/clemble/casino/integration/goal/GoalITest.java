package com.clemble.casino.integration.goal;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.GoalState;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

/**
 * Created by mavarazy on 8/2/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class GoalITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void testAddGoal() {
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        Goal goal = new Goal(A.getPlayer(), null, "Pending A goal", Money.create(Currency.FakeMoney, 100), new Date(System.currentTimeMillis()), 10, GoalState.pending);
        Goal savedGoal = A.goalOperations().addMyGoal(goal);

        Assert.assertEquals(A.goalOperations().myGoals().size(), 1);
        Assert.assertEquals(A.goalOperations().myGoals().iterator().next(), goal);
    }

}
