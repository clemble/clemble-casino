package com.clemble.casino.server.spring.web.management;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.configuration.GameLocation;
import com.clemble.casino.configuration.ResourceLocationService;
import com.clemble.casino.game.Game;
import com.clemble.casino.server.ServerRegistry;
import com.clemble.casino.server.configuration.ServerLocation;
import com.clemble.casino.server.configuration.SimpleNotificationConfigurationService;
import com.clemble.casino.server.configuration.SimpleResourceLocationController;
import com.clemble.casino.server.player.notification.PlayerNotificationRegistry;
import com.clemble.casino.server.player.notification.SimplePlayerNotificationRegistry;
import com.google.common.collect.ImmutableList;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = ManagementWebSpringConfigurationInitializationTest.TestManagementWebSpringConfiguration.class)
public class ManagementWebSpringConfigurationInitializationTest {

    @Configuration
    public static class TestManagementWebSpringConfiguration extends AbstractManagementWebSpringConfiguration {

        @Bean
        public ServerLocation paymentEndpointRegistry(){
            return new ServerLocation("http://localhost:8080/payment-web/");
        }

        @Bean
        public PlayerNotificationRegistry playerNotificationRegistry() {
            return new SimplePlayerNotificationRegistry(new ServerRegistry(Collections.singletonList("localhost")));
        }

        @Bean
        public ResourceLocationService resourceLocationService() {
            SimpleNotificationConfigurationService configurationService = new SimpleNotificationConfigurationService("guest", "guest", playerNotificationRegistry());
            return new SimpleResourceLocationController(configurationService,
                    "http://localhost:8080/player-web/",
                    "http://localhost:8080/payment-web/",
                    ImmutableList.<GameLocation> of(new GameLocation(Game.num, "http://localhost:8080/integration-game/")));
        }

    }

    @Test
    public void initialized() {
    }

}
