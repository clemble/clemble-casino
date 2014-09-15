package com.clemble.casino.integration.goal;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.GoalJudgeInvitation;
import com.clemble.casino.goal.GoalRequest;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.test.concurrent.AsyncCompletionUtils;
import com.clemble.test.concurrent.Check;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by mavarazy on 8/18/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class GoalJudgeInvitationITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void testInvitation() {
        String goalStr = "Run 30K";
        // Step 1. Creating 2 players
        final ClembleCasinoOperations A = playerScenarios.createPlayer();
        final ClembleCasinoOperations B = playerScenarios.createPlayer();
        // Step 1.1 Checking A & B are empty
        Assert.assertEquals(B.goalOperations().goalInvitationOperations().myPending().size(), 0);
        Assert.assertEquals(A.goalOperations().goalInvitationOperations().myPending().size(), 0);
        // Step 2. Creating invitation
        Goal goal = A.goalOperations().goalService().addMyGoal(new GoalRequest(B.getPlayer(), goalStr, 7, Money.create(Currency.FakeMoney, 50)));
        Assert.assertNotNull(goal);
        Assert.assertEquals(A.goalOperations().goalService().myPendingGoals().size(), 1);
        // Step 3. Checking invitation created for the judge
        AsyncCompletionUtils.check(new Check() {
            @Override
            public boolean check() {
                return B.goalOperations().goalInvitationOperations().myPending().size() == 1;
            }
        }, 30_000, 1_000);
        // Step 4. Checking invitations
        GoalJudgeInvitation AtoBinvitation = B.goalOperations().goalInvitationOperations().myPending().iterator().next();
        Assert.assertEquals(AtoBinvitation.getGoal(), goalStr);
    }
}
