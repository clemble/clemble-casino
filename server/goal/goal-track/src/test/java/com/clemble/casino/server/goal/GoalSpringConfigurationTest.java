package com.clemble.casino.server.goal;

import com.clemble.casino.server.goal.spring.GoalSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by mavarazy on 8/2/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = GoalSpringConfiguration.class)
public class GoalSpringConfigurationTest {

    @Test
    public void test(){
    }

}
