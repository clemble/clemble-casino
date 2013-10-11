package com.clemble.casino.server.social;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.social.SocialModuleSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(SpringConfiguration.UNIT_TEST)
@ContextConfiguration(classes = SocialModuleSpringConfiguration.class)
public class InitializationTest {

    @Test
    public void testInitialized() {
    }
}
