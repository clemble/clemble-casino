package com.clemble.casino.goal;

import com.clemble.casino.goal.repository.GoalJudgeDutyRepository;
import com.clemble.casino.goal.spring.GoalJudgeDutySpringConfiguration;
import com.clemble.test.random.ObjectGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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

}
