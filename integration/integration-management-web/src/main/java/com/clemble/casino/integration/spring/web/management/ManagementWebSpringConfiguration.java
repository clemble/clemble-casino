package com.clemble.casino.integration.spring.web.management;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.clemble.casino.configuration.GameLocation;
import com.clemble.casino.configuration.ResourceLocationService;
import com.clemble.casino.game.Game;
import com.clemble.casino.server.ServerRegistry;
import com.clemble.casino.server.configuration.ServerLocation;
import com.clemble.casino.server.configuration.SimpleNotificationConfigurationService;
import com.clemble.casino.server.configuration.SimpleResourceLocationController;
import com.clemble.casino.server.player.notification.PlayerNotificationRegistry;
import com.clemble.casino.server.player.notification.SimplePlayerNotificationRegistry;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.payment.PaymentCommonSpringConfiguration;
import com.clemble.casino.server.spring.player.PlayerCommonSpringConfiguration;
import com.clemble.casino.server.spring.web.management.AbstractManagementWebSpringConfiguration;
import com.google.common.collect.ImmutableList;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class, PaymentCommonSpringConfiguration.class,
        ManagementWebSpringConfiguration.DefaultAndTest.class, ManagementWebSpringConfiguration.Cloud.class, ManagementWebSpringConfiguration.Integration.class })
public class ManagementWebSpringConfiguration extends AbstractManagementWebSpringConfiguration {

    @Configuration
    @Profile({ UNIT_TEST, DEFAULT, INTEGRATION_DEFAULT })
    public static class DefaultAndTest {

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

    @Configuration
    @Profile(INTEGRATION_TEST)
    public static class Integration {

        @Bean
        public ServerLocation paymentEndpointRegistry(){
            return new ServerLocation("http://localhost:9999/payment-web/");
        }

        @Bean
        public PlayerNotificationRegistry playerNotificationRegistry() {
            return new SimplePlayerNotificationRegistry(new ServerRegistry(Collections.singletonList("localhost")));
        }

        @Bean
        public ResourceLocationService resourceLocationService() {
            SimpleNotificationConfigurationService configurationService = new SimpleNotificationConfigurationService("guest", "guest", playerNotificationRegistry());
            return new SimpleResourceLocationController(configurationService,
                    "http://localhost:9999/player-web/",
                    "http://localhost:9999/payment-web/",
                    ImmutableList.<GameLocation> of(new GameLocation(Game.num, "http://localhost:9999/integration-game/")));
        }

    }

    @Configuration
    @Profile(CLOUD)
    public static class Cloud {

        @Bean
        public ServerLocation paymentEndpointRegistry(){
            return new ServerLocation("http://ec2-50-16-93-157.compute-1.amazonaws.com/payment-web/");
        }

        @Bean
        public PlayerNotificationRegistry playerNotificationRegistry() {
            return new SimplePlayerNotificationRegistry(new ServerRegistry(Collections.singletonList("ec2-50-16-93-157.compute-1.amazonaws.com")));
        }

        @Bean
        public ResourceLocationService resourceLocationService() {
            SimpleNotificationConfigurationService configurationService = new SimpleNotificationConfigurationService("guest", "guest", playerNotificationRegistry());
            return new SimpleResourceLocationController(configurationService,
                    "http://ec2-50-16-93-157.compute-1.amazonaws.com/player-web/",
                    "http://ec2-50-16-93-157.compute-1.amazonaws.com/payment-web/",
                    ImmutableList.<GameLocation> of(new GameLocation(Game.num, "http://ec2-50-16-93-157.compute-1.amazonaws.com/integration-game/")));
        }

    }

}
