package com.clemble.casino.server.phone;

import com.clemble.casino.server.phone.service.ServerSMSSender;
import com.clemble.casino.server.phone.spring.PlayerPhoneSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mavarazy on 12/8/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PlayerPhoneSpringConfiguration.class)
public class PlayerPhoneSpringConfigurationTest {

    @Autowired
    public ServerSMSSender smsSender;

    @Test
    public void testSMSSend(){
        smsSender.send("+677989809070951", "Test message");
    }

}

