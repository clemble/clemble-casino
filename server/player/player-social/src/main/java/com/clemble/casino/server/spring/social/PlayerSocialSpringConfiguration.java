package com.clemble.casino.server.spring.social;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import com.clemble.casino.server.player.PlayerIdGenerator;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.SystemNotificationService;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;
import com.clemble.casino.server.social.SocialConnectionAdapterRegistry;
import com.clemble.casino.server.social.SocialConnectionDataAdapter;
import com.clemble.casino.server.social.SocialNetworkPopulatorEventListener;
import com.clemble.casino.server.social.SocialPlayerDiscoveredConnectionEventListener;
import com.clemble.casino.server.social.SocialProfileConnectionSignUp;
import com.clemble.casino.server.social.adapter.FacebookSocialAdapter;
import com.clemble.casino.server.social.adapter.LinkedInSocialAdapter;
import com.clemble.casino.server.social.adapter.TwitterSocialAdapter;
import com.clemble.casino.server.spring.common.BasicJPASpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.player.PlayerManagementSpringConfiguration;

@Configuration
@Import(value = {
    PlayerManagementSpringConfiguration.class,
//    BasicJPASpringConfiguration.class
})
public class PlayerSocialSpringConfiguration implements SpringConfiguration {

    @Bean
    public SocialConnectionDataAdapter socialConnectionDataAdapter(
            UsersConnectionRepository usersConnectionRepository,
            SocialConnectionAdapterRegistry socialConnectionAdapterRegistry,
            ConnectionFactoryRegistry connectionFactoryLocator,
            SystemNotificationService systemNotificationService) {
        return new SocialConnectionDataAdapter(connectionFactoryLocator, usersConnectionRepository, socialConnectionAdapterRegistry, systemNotificationService);
    }

    @Bean
    public ConnectionFactoryRegistry connectionFactoryLocator(
            SocialConnectionAdapterRegistry socialConnectionAdapterRegistry,
            FacebookConnectionFactory facebookConnectionFactory,
            LinkedInConnectionFactory linkedInConnectionFactory,
            TwitterConnectionFactory twitterConnectionFactory) {
        ConnectionFactoryRegistry connectionFactoryRegistry = new ConnectionFactoryRegistry();
        // Step 1. Registering FB
        connectionFactoryRegistry.addConnectionFactory(facebookConnectionFactory);
        socialConnectionAdapterRegistry.register(new FacebookSocialAdapter(facebookConnectionFactory));
        // Step 2. Registering Twitter
        connectionFactoryRegistry.addConnectionFactory(twitterConnectionFactory);
        socialConnectionAdapterRegistry.register(new TwitterSocialAdapter(twitterConnectionFactory));
        // Step 3. Registering LinkedIn
        connectionFactoryRegistry.addConnectionFactory(linkedInConnectionFactory);
        socialConnectionAdapterRegistry.register(new LinkedInSocialAdapter(linkedInConnectionFactory));
        return connectionFactoryRegistry;
    }

    @Bean
    public SocialNetworkPopulatorEventListener socialNetworkPopulator(
            SocialConnectionAdapterRegistry socialAdapterRegistry,
            PlayerSocialNetworkRepository socialNetworkRepository,
            SystemNotificationService notificationService,
            UsersConnectionRepository usersConnectionRepository,
            SystemNotificationServiceListener serviceListener) {
        SocialNetworkPopulatorEventListener networkPopulator = new SocialNetworkPopulatorEventListener(socialAdapterRegistry, usersConnectionRepository, socialNetworkRepository, notificationService);
        serviceListener.subscribe(networkPopulator);
        return networkPopulator;
    }

    @Bean
    public FacebookConnectionFactory facebookConnectionFactory(
            @Value("${clemble.social.facebook.key}") String key,
            @Value("${clemble.social.facebook.secret}") String secret) {
        return new FacebookConnectionFactory(key, secret);
    }

    @Bean
    public TwitterConnectionFactory twitterConnectionFactory(
            @Value("${clemble.social.twitter.key}") String key,
            @Value("${clemble.social.twitter.secret}") String secret) {
        return new TwitterConnectionFactory(key, secret);
    }

    @Bean
    public LinkedInConnectionFactory linkedInConnectionFactory(
        @Value("${clemble.social.linkedin.key}") String key,
        @Value("${clemble.social.linkedin.secret}") String secret) {
        return new LinkedInConnectionFactory(key, secret);
    }

    @Bean
    public SocialConnectionAdapterRegistry socialAdapterRegistry() {
        return new SocialConnectionAdapterRegistry();
    }

    @Bean
    public SocialPlayerDiscoveredConnectionEventListener discoveryNotifier(@Qualifier("playerNotificationService") PlayerNotificationService notificationService, SystemNotificationServiceListener systemListener){
        SocialPlayerDiscoveredConnectionEventListener discoveryNotifier = new SocialPlayerDiscoveredConnectionEventListener(notificationService);
        systemListener.subscribe(discoveryNotifier);
        return discoveryNotifier;
    }

    @Bean
    public ConnectionSignUp connectionSignUp(
            PlayerIdGenerator idGenerator,
            PlayerProfileRepository profileRepository,
            SocialConnectionAdapterRegistry socialAdapterRegistry,
            PlayerSocialNetworkRepository socialNetworkRepository) {
        ConnectionSignUp signUp = new SocialProfileConnectionSignUp(idGenerator, profileRepository, socialAdapterRegistry, socialNetworkRepository);
        return signUp;
    }

    @Bean
    public UsersConnectionRepository usersConnectionRepository(
//            @Qualifier("dataSource") DataSource dataSource,
            ConnectionSignUp connectionSignUp,
            ConnectionFactoryRegistry connectionFactoryLocator) {
        InMemoryUsersConnectionRepository repository = new InMemoryUsersConnectionRepository(connectionFactoryLocator);
        repository.setConnectionSignUp(connectionSignUp);
//        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
//        repository.setConnectionSignUp(connectionSignUp);
        return repository;
    }

}
