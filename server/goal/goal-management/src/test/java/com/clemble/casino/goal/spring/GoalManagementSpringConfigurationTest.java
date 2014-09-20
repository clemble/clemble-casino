package com.clemble.casino.goal.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by mavarazy on 9/20/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { GoalManagementSpringConfiguration.class })
public class GoalManagementSpringConfigurationTest {

    @Test
    public void test() {
    }

}
