package com.clemble.casino.goal;

import com.clemble.casino.goal.repository.GoalJudgeDutyRepository;
import com.clemble.casino.goal.spring.GoalJudgeDutySpringConfiguration;
import com.clemble.test.concurrent.AsyncCompletionUtils;
import com.clemble.test.concurrent.Check;
import com.clemble.test.random.ObjectGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 8/23/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GoalJudgeDutySpringConfiguration.class)
@WebAppConfiguration
public class GoalJudgeDutySpringConfigurationTest {

    @Autowired
    public GoalJudgeDutyRepository dutyRepository;

    @Test
    public void testInitialization() {
        // Step 1. Generating duty
        GoalJudgeDuty duty = ObjectGenerator.generate(GoalJudgeDuty.class);
        // Step 2. Saving duty
        dutyRepository.save(duty);
        // Step 3. Checking duty returned
        Assert.assertEquals(dutyRepository.findByJudge(duty.getJudge()).iterator().next(), duty);
    }

    @Test
    public void testLookup() {
        // Step 1. Generating duty
        GoalJudgeDuty duty = new GoalJudgeDuty(
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            GoalJudgeDutyStatus.pending,
            new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
        );
        Date now = new Date();
        // Step 2. Saving duty
        dutyRepository.save(duty);
        // Step 3. Checking duty returned
        Assert.assertEquals(dutyRepository.findByJudge(duty.getJudge()).iterator().next(), duty);
        // Step 4. Find outdated tasks
        List<GoalJudgeDuty> dueDuties = dutyRepository.findByStatusAndDueDateBefore(GoalJudgeDutyStatus.pending, now);
        Assert.assertTrue(dueDuties.contains(duty));
    }

    @Test
    public void testDueDateTasks() {
        // Step 1. Generating duty
        GoalJudgeDuty duty = new GoalJudgeDuty(
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            GoalJudgeDutyStatus.pending,
            new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
        );
        Date now = new Date();
        // Step 2. Saving duty
        final String goalKey = duty.getGoalKey();
        dutyRepository.save(duty);
        // Step 3. Checking duty returned
        AsyncCompletionUtils.check(new Check() {
            @Override
            public boolean check() {
                return dutyRepository.findOne(goalKey).getStatus() == GoalJudgeDutyStatus.due;
            }
        }, 165_000, 1_000);
        Assert.assertEquals(dutyRepository.findByJudge(duty.getJudge()).iterator().next(), duty.cloneWithStatus(GoalJudgeDutyStatus.due));
    }

}
