package com.clemble.casino.goal;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.goal.controller.GoalJudgeInvitationServiceController;
import com.clemble.casino.goal.repository.GoalJudgeInvitationRepository;
import com.clemble.casino.goal.spring.GoalJudgeSpringConfiguration;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.test.random.ObjectGenerator;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 8/18/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GoalJudgeSpringConfiguration.class)
@WebAppConfiguration
public class GoalJudgeInvitationTest {

    @Autowired
    public GoalJudgeInvitationRepository invitationRepository;

    @Autowired
    public GoalJudgeInvitationServiceController invitationService;

    @Test
    public void testDuties() {
        // Step 1. Generating GoalRequest
        String A = ObjectGenerator.generate(String.class);
        String B = ObjectGenerator.generate(String.class);
        Goal goal = new Goal(ObjectGenerator.generate(String.class),
            A,
            B,
            "Run 10K",
            new Date(),
            new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)),
            GoalState.pending,
            new GoalStatus("On my way", new Date()),
            new Bid(Money.create(Currency.FakeMoney, 10), Money.create(Currency.FakeMoney, 20)));
        // Step 2. Generating and saving invitation out of this goal request
        GoalJudgeInvitation invitation = GoalJudgeInvitation.fromGoal(goal);
        invitationRepository.save(invitation);
        // Step 3. Checking proper duties returned by controller
        Assert.assertEquals(invitationService.myPending(B).iterator().next(), invitation);
    }

    @Test
    public void testInvitations() {
        // Step 1. Generating GoalRequest
        String A = ObjectGenerator.generate(String.class);
        String B = ObjectGenerator.generate(String.class);
        Goal goal = new Goal(ObjectGenerator.generate(String.class),
            A,
            B,
            "Run 10K",
            new Date(),
            new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)),
            GoalState.pending,
            new GoalStatus("On my way", new Date()),
            new Bid(Money.create(Currency.FakeMoney, 10), Money.create(Currency.FakeMoney, 20)));
        // Step 2. Generating and saving invitation out of this goal request
        GoalJudgeInvitation invitation = GoalJudgeInvitation.fromGoal(goal);
        invitationRepository.save(invitation);
        // Step 3. Checking proper duties returned by controller
        Assert.assertEquals(invitationService.myPending(B).iterator().next(), invitation);
    }

    @Ignore
    public void testInvitationsAndDuties() {
        // Step 1. Generating GoalRequest
        String A = ObjectGenerator.generate(String.class);
        String B = ObjectGenerator.generate(String.class);
        Goal goalA = new Goal(ObjectGenerator.generate(String.class),
            A,
            B,
            "Run 10K",
            new Date(),
            new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)),
            GoalState.pending,
            new GoalStatus("On my way", new Date()),
            new Bid(Money.create(Currency.FakeMoney, 10), Money.create(Currency.FakeMoney, 20)));
        // Step 2. Generating and saving invitation out of this goal request
        GoalJudgeInvitation invitationA = GoalJudgeInvitation.fromGoal(goalA);
        invitationRepository.save(invitationA);
        // Step 3. Create goal by B
        Goal goalB = new Goal(ObjectGenerator.generate(String.class),
            B,
            A,
            "Run 10K",
            new Date(),
            new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)),
            GoalState.pending,
            new GoalStatus("On my way", new Date()),
            new Bid(Money.create(Currency.FakeMoney, 10), Money.create(Currency.FakeMoney, 20)));
        // Step 4. Generating and saving invitation out of this goal request
        GoalJudgeInvitation invitationB = GoalJudgeInvitation.fromGoal(goalA);
        invitationRepository.save(invitationB);
        // Step 5. Checking proper duties returned by controller
//        Collection<GoalJudgeInvitation> invitations = invitationService.myDutiesAndInvitations(A);
//        Assert.assertTrue(invitations.contains(invitationA));
//        Assert.assertTrue(invitations.contains(invitationB));
//
//        invitations = invitationService.myDutiesAndInvitations(B);
//        Assert.assertTrue(invitations.contains(invitationA));
//        Assert.assertTrue(invitations.contains(invitationB));
    }


}
