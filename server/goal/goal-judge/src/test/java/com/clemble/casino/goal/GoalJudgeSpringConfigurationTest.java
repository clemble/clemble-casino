package com.clemble.casino.goal;

import com.clemble.casino.goal.spring.GoalJudgeSpringConfiguration;
import com.clemble.test.random.ObjectGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by mavarazy on 8/18/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GoalJudgeSpringConfiguration.class)
@WebAppConfiguration
public class GoalJudgeSpringConfigurationTest {

    @Test
    public void testInitialized() {
    }
}
