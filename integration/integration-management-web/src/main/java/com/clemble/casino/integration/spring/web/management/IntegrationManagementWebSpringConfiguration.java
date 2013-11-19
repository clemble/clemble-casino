package com.clemble.casino.integration.spring.web.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.clemble.casino.configuration.ResourceLocationService;
import com.clemble.casino.configuration.ServerRegistryConfiguration;
import com.clemble.casino.game.Game;
import com.clemble.casino.server.configuration.SimpleNotificationConfigurationService;
import com.clemble.casino.server.configuration.SimpleResourceLocationController;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.payment.PaymentCommonSpringConfiguration;
import com.clemble.casino.server.spring.player.PlayerCommonSpringConfiguration;
import com.clemble.casino.server.spring.web.management.AbstractManagementWebSpringConfiguration;
import com.google.common.collect.ImmutableList;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class, PaymentCommonSpringConfiguration.class,
        IntegrationManagementWebSpringConfiguration.DefaultAndTest.class, IntegrationManagementWebSpringConfiguration.Cloud.class, IntegrationManagementWebSpringConfiguration.Integration.class })
public class IntegrationManagementWebSpringConfiguration extends AbstractManagementWebSpringConfiguration {

    @Configuration
    @Profile({ UNIT_TEST, DEFAULT, INTEGRATION_DEFAULT })
    public static class DefaultAndTest {

        @Autowired
        public ServerRegistryConfiguration serverRegistryConfiguration;


        @Bean
        public ResourceLocationService resourceLocationService() {
            SimpleNotificationConfigurationService configurationService = new SimpleNotificationConfigurationService("guest", "guest", serverRegistryConfiguration.getPlayerNotificationRegistry());
            return new SimpleResourceLocationController(configurationService,
                    serverRegistryConfiguration,
                    ImmutableList.<Game> of(Game.num));
        }

    }

    @Configuration
    @Profile(INTEGRATION_TEST)
    public static class Integration {

        @Autowired
        public ServerRegistryConfiguration serverRegistryConfiguration;

        @Bean
        public ResourceLocationService resourceLocationService() {
            SimpleNotificationConfigurationService configurationService = new SimpleNotificationConfigurationService("guest", "guest", serverRegistryConfiguration.getPlayerNotificationRegistry());
            return new SimpleResourceLocationController(configurationService,
                    serverRegistryConfiguration,
                    ImmutableList.<Game> of(Game.num));
        }

    }

    @Configuration
    @Profile(CLOUD)
    public static class Cloud {
        
        @Autowired
        public ServerRegistryConfiguration serverRegistryConfiguration;

        @Bean
        public ResourceLocationService resourceLocationService() {
            SimpleNotificationConfigurationService configurationService = new SimpleNotificationConfigurationService("guest", "guest", serverRegistryConfiguration.getPlayerNotificationRegistry());
            return new SimpleResourceLocationController(configurationService,
                    serverRegistryConfiguration,
                    ImmutableList.<Game> of(Game.num));
        }

    }

}
