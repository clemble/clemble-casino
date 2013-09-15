package com.gogomaya.server.spring.web.management;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.gogomaya.configuration.GameLocation;
import com.gogomaya.configuration.ResourceLocationService;
import com.gogomaya.game.Game;
import com.gogomaya.server.ServerRegistry;
import com.gogomaya.server.configuration.ServerLocation;
import com.gogomaya.server.configuration.SimpleNotificationConfigurationService;
import com.gogomaya.server.configuration.SimpleResourceLocationController;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.gogomaya.server.player.notification.SimplePlayerNotificationRegistry;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;
import com.gogomaya.server.spring.payment.PaymentCommonSpringConfiguration;
import com.gogomaya.server.spring.player.PlayerCommonSpringConfiguration;
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
            final ServerRegistry serverRegistry = new ServerRegistry();
            serverRegistry.register(1_000_000L, "localhost");
            return new SimplePlayerNotificationRegistry(serverRegistry);
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
            final ServerRegistry serverRegistry = new ServerRegistry();
            serverRegistry.register(1_000_000L, "localhost");
            return new SimplePlayerNotificationRegistry(serverRegistry);
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
            final ServerRegistry serverRegistry = new ServerRegistry();
            serverRegistry.register(1_000_000L, "ec2-50-16-93-157.compute-1.amazonaws.com");
            return new SimplePlayerNotificationRegistry(serverRegistry);
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
