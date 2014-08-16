package com.clemble.casino.integration.goal;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.GoalKey;
import com.clemble.casino.goal.GoalState;
import com.clemble.casino.goal.GoalStatus;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.integration.util.ClembleCasinoExceptionMatcherFactory;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.test.random.ObjectGenerator;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 8/2/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class GoalITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testAddGoal() {
        // Step 1. Creating Player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 2. Creating random goal
        check(A, "GOAL_1", A.getPlayer());
    }

    @Test
    public void testAddGoalWithoutGoalId() {
        // Step 1. Generating Player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 2. Creating goal without ID
        check(A, null, A.getPlayer());
    }

    @Test
    public void testAddGoalWithoutGoalAndPlayerId() {
        // Step 1. Generating Player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 2. Creating goal without ID
        check(A, null, null);
    }

    @Test
    public void testAddGoalWithAnotherPlayer() {
        // Step 1. Creating Player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 2. Creating random goal
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.GoalPlayerIncorrect));
        check(A, "GOAL_1", ObjectGenerator.generate(String.class));
    }

    @Test
    public void testAddGoalWithoutGoalIdWithAnotherPlayer() {
        // Step 1. Generating Player
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 2. Creating goal without ID
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.GoalPlayerIncorrect));
        check(A, null, ObjectGenerator.generate(String.class));
    }

    @Test
    public void testSavingInMissedState() {
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 1. Creating random goal
        Goal goalToSave = new Goal(null, null, "Pending A goal", null, new Date(System.currentTimeMillis() + 10_000), GoalState.missed, null);
        // Step 3. Saving and checking goal state is valid
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.GoalStateIncorrect));
        // Step 4. Trying to save new value
        A.goalOperations().addMyGoal(goalToSave);
    }

    @Test
    public void testSavingInReachedState() {
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 1. Creating random goal
        Goal goalToSave = new Goal(null, null, "Pending A goal", null, new Date(System.currentTimeMillis()), GoalState.reached, null);
        // Step 3. Saving and checking goal state is valid
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.GoalStateIncorrect));
        // Step 4. Trying to save new value
        A.goalOperations().addMyGoal(goalToSave);
    }

    @Test
    public void testSavingWithPassedDueDate() {
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 1. Creating random goal
        Goal goalToSave = new Goal(null, null, "Pending A goal", null, new Date(0), GoalState.pending, null);
        // Step 3. Saving and checking goal state is valid
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.GoalDueDateInPast));
        // Step 4. Trying to save new value
        A.goalOperations().addMyGoal(goalToSave);
    }

    @Test
    public void testUpdateGoalStatus() {
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 1. Creating random goal
        Goal goal = new Goal(null, null, "Pending A goal", null, new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)), GoalState.pending, null);
        // Step 2. Creating goal save
        Goal savedGoal = A.goalOperations().addMyGoal(goal);
        // Step 3. Updating goal status
        GoalStatus status = A.goalOperations().updateMyGoal(savedGoal.getGoalKey().getGoal(), GoalStatus.create("Test Update"));
        // Step 4. Checking goal status updated
        Goal updatedGoal = A.goalOperations().myGoal(savedGoal.getGoalKey().getGoal());
        // Step 5. Checking goal has a new status
        assertEquals(updatedGoal.getStatus().getStatus(), "Test Update");
    }



    public void check(ClembleCasinoOperations A, String goalKey, String player) {
        // Step 1. Creating random goal
        Bid bid = new Bid(A.getPlayer(), A.getPlayer(), Money.create(Currency.FakeMoney, 40));
        Goal goalToSave = new Goal(new GoalKey(player, goalKey), player, "Pending A goal", null, new Date(System.currentTimeMillis() + 10_000), GoalState.pending, null);
        // Step 3. Saving and checking goal is valid
        Goal savedGoal = A.goalOperations().addMyGoal(goalToSave);
        assertNotNull(savedGoal);
        assertEquals(savedGoal.getGoalKey().getPlayer(), A.getPlayer());
        assertNotEquals(savedGoal.getGoalKey(), goalKey);
        assertEquals(savedGoal, goalToSave.cloneWithPlayerAndGoal(savedGoal.getGoalKey().getPlayer(), savedGoal.getGoalKey().getGoal(), GoalState.pending));
        // Step 4. Checking GoalOperations return goal
        assertEquals(A.goalOperations().myGoals().size(), 1);
        Goal resGoal = A.goalOperations().myGoals().iterator().next();
        assertNotNull(resGoal);
        assertEquals(resGoal.getGoalKey().getPlayer(), A.getPlayer());
        assertEquals(resGoal, savedGoal);
    }

}
