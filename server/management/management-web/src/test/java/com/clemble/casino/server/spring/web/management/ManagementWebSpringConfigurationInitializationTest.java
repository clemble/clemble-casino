package com.clemble.casino.server.spring.web.management;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ManagementWebSpringConfiguration.class })
public class ManagementWebSpringConfigurationInitializationTest {

    @Test
    public void initialized() {
    }

}
