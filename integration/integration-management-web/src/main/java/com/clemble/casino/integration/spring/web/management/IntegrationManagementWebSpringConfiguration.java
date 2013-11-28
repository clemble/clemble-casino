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
import com.clemble.casino.server.configuration.SimpleResourceLocationService;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.payment.PaymentCommonSpringConfiguration;
import com.clemble.casino.server.spring.player.PlayerCommonSpringConfiguration;
import com.clemble.casino.server.spring.web.management.AbstractManagementWebSpringConfiguration;
import com.google.common.collect.ImmutableList;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class, PaymentCommonSpringConfiguration.class, IntegrationManagementWebSpringConfiguration.DefaultAndTest.class })
public class IntegrationManagementWebSpringConfiguration extends AbstractManagementWebSpringConfiguration {

    @Configuration
    @Profile({ UNIT_TEST, DEFAULT, INTEGRATION_DEFAULT, INTEGRATION_TEST, INTEGRATION_CLOUD })
    public static class DefaultAndTest {

        @Bean
        public ServerRegistryConfiguration serverRegistryConfiguration() {
            return new ServerRegistryConfiguration("localhost", "http://localhost:8080/player/", "http://localhost:8080/payment/", "http://localhost:8080/game/");
        }


        @Bean
        @Autowired
        public ResourceLocationService resourceLocationService(ServerRegistryConfiguration serverRegistryConfiguration) {
            SimpleNotificationConfigurationService configurationService = new SimpleNotificationConfigurationService("guest", "guest", serverRegistryConfiguration.getPlayerNotificationRegistry());
            return new SimpleResourceLocationService(configurationService,
                    serverRegistryConfiguration,
                    ImmutableList.<Game> of(Game.num));
        }

    }

}
