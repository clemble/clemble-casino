package com.clemble.casino.server.email;

import com.clemble.casino.server.email.service.MandrillPlayerEmailService;
import com.clemble.casino.server.email.spring.PlayerEmailSpringConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mavarazy on 12/6/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Ignore // This is just to test notification works, nothing else
@ContextConfiguration(classes = PlayerEmailSpringConfiguration.class)
public class TestSimpleEmailVerification {

    @Autowired
    public MandrillPlayerEmailService playerEmailService;

    @Test
    public void test() {
        playerEmailService.requestVerification("mavarazy@gmail.com");
    }

}
