package com.clemble.casino.server.web.management;

import com.clemble.casino.server.spring.web.management.RegistrationSpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by mavarazy on 7/4/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { RegistrationSpringConfiguration.class })
public class RegistrationSpringConfigurationTest {

    @Test
    public void initialized(){
    }

}
