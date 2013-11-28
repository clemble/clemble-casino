package com.clemble.casino.server.spring.web.management;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.configuration.ResourceLocationService;
import com.clemble.casino.configuration.ServerRegistryConfiguration;
import com.clemble.casino.game.Game;
import com.clemble.casino.server.configuration.SimpleNotificationConfigurationService;
import com.clemble.casino.server.configuration.SimpleResourceLocationService;
import com.clemble.casino.server.spring.web.OAuthSpringConfiguration;
import com.google.common.collect.ImmutableList;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ManagementWebSpringConfigurationInitializationTest.TestManagementWebSpringConfiguration.class })
public class ManagementWebSpringConfigurationInitializationTest {

    @Configuration
    @Import({OAuthSpringConfiguration.class})
    public static class TestManagementWebSpringConfiguration extends AbstractManagementWebSpringConfiguration {
        
        @Bean
        public ServerRegistryConfiguration serverRegistryConfiguration() {
            return new ServerRegistryConfiguration("localhost");
        }

        @Bean
        public ResourceLocationService resourceLocationService(ServerRegistryConfiguration serverRegistryConfiguration) {
            SimpleNotificationConfigurationService configurationService = new SimpleNotificationConfigurationService("guest", "guest", serverRegistryConfiguration.getPlayerNotificationRegistry());
            return new SimpleResourceLocationService(configurationService, serverRegistryConfiguration, ImmutableList.<Game> of(Game.num));
        }
    }

    @Test
    public void initialized() {
    }

}
