package com.clemble.casino.server.social;

import com.clemble.casino.server.social.spring.PlayerSocialSpringConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { PlayerSocialSpringConfiguration.class })
public class PlayerSocialSpringConfigurationTest {

    @Autowired
    public ProviderSignInController signInController;

    @Test
    public void testInitialized() {
        Assert.assertNotNull(signInController);
    }

}
