package com.clemble.casino.suggestion;

import com.clemble.casino.goal.suggestion.spring.GoalSuggestionSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mavarazy on 1/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GoalSuggestionSpringConfiguration.class)
public class GoalSuggestionSpringConfigurationTest {

    @Test
    public void simpleInitializationTest(){
    }

}
