package com.clemble.casino.integration.goal;

import com.clemble.casino.bet.PlayerBid;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.goal.*;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.integration.util.ClembleCasinoExceptionMatcherFactory;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.test.random.ObjectGenerator;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
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
    @Ignore // No longer applicable, goal state management is now completely on server
    public void testSavingInMissedState() {
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 1. Creating random goal
        GoalRequest goalToSave = new GoalRequest(null, "Pending A goal", 7, Money.create(Currency.FakeMoney, 100));
        // Step 3. Saving and checking goal state is valid
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.GoalStateIncorrect));
        // Step 4. Trying to save new value
        A.goalOperations().addMyGoal(goalToSave);
    }

    @Test
    @Ignore // No longer applicable, goal state management is now completely on server
    public void testSavingInReachedState() {
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 1. Creating random goal
        GoalRequest goalToSave = new GoalRequest(null, "Pending A goal", 0, Money.create(Currency.FakeMoney, 100));
        // Step 3. Saving and checking goal state is valid
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.GoalStateIncorrect));
        // Step 4. Trying to save new value
        A.goalOperations().addMyGoal(goalToSave);
    }

    @Test
    public void testSavingWithPassedDueDate() {
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 1. Creating random goal
        GoalRequest goalToSave = new GoalRequest(null, "Pending A goal", 0, Money.create(Currency.FakeMoney, 100));
        // Step 3. Saving and checking goal state is valid
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.GoalDueDateInPast));
        // Step 4. Trying to save new value
        A.goalOperations().addMyGoal(goalToSave);
    }

    @Test
    public void testUpdateGoalStatus() {
        ClembleCasinoOperations A = playerScenarios.createPlayer();
        // Step 1. Creating random goal
        GoalRequest goalToSave = new GoalRequest(null, "Pending A goal", 1, Money.create(Currency.FakeMoney, 100));
        // Step 2. Creating goal save
        Goal savedGoal = A.goalOperations().addMyGoal(goalToSave);
        // Step 3. Updating goal status
        GoalStatus status = A.goalOperations().updateMyGoal(savedGoal.getGoalKey().getId(), GoalStatus.create("Test Update"));
        // Step 4. Checking goal status updated
        Goal updatedGoal = A.goalOperations().myGoal(savedGoal.getGoalKey().getId());
        // Step 5. Checking goal has a new status
        assertEquals(updatedGoal.getStatus().getStatus(), "Test Update");
    }



    public void check(ClembleCasinoOperations A, String goalKey, String player) {
        // Step 1. Creating random goal
        GoalRequest goalToSave = new GoalRequest(player, "Pending A goal", 1, Money.create(Currency.FakeMoney, 100));
        // Step 3. Saving and checking goal is valid
        Goal savedGoal = A.goalOperations().addMyGoal(goalToSave);
        assertNotNull(savedGoal);
        assertEquals(savedGoal.getGoalKey().getPlayer(), A.getPlayer());
        assertNotEquals(savedGoal.getGoalKey(), goalKey);
        assertEquals(savedGoal.getDescription(), goalToSave.getGoal());
        assertEquals(savedGoal.getPlayer(), A.getPlayer());
        // Step 4. Checking GoalOperations return goal
        assertEquals(A.goalOperations().myGoals().size(), 1);
        Goal resGoal = A.goalOperations().myGoals().iterator().next();
        assertNotNull(resGoal);
        assertEquals(resGoal.getGoalKey().getPlayer(), A.getPlayer());
        assertEquals(resGoal, savedGoal);
    }

}
