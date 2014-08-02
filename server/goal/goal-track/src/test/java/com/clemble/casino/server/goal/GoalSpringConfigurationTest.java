package com.clemble.casino.server.goal;

import com.clemble.casino.server.goal.spring.GoalSpringConfiguration;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by mavarazy on 8/2/14.
 */
@WebAppConfiguration
@ContextConfiguration(classes = GoalSpringConfiguration.class)
public class GoalSpringConfigurationTest {

    @Test
    public void test(){
    }

}
