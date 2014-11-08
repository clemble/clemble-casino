package com.clemble.casino.schedule.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mavarazy on 11/8/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ScheduleSpringConfiguration.class})
public class ScheduleSpringConfigurationTest {

    @Test
    public void testInitialized() {
    }

}
