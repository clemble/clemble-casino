package com.clemble.casino.server.social.spring;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.server.social.ServerProfileSocialRegistrationService;
import com.clemble.casino.server.security.PlayerTokenFactory;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.PlayerTokenSpringConfiguration;
import com.clemble.casino.server.social.controller.PlayerSocialRegistrationController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.social.SocialConnectionAdapterRegistry;
import com.clemble.casino.server.social.SocialConnectionDataAdapter;
import com.clemble.casino.server.social.SocialNetworkPopulatorEventListener;
import com.clemble.casino.server.social.PlayerSocialKeyGenerator;
import com.clemble.casino.server.social.adapter.FacebookSocialAdapter;
import com.clemble.casino.server.social.adapter.LinkedInSocialAdapter;
import com.clemble.casino.server.social.adapter.TwitterSocialAdapter;
import com.clemble.casino.server.spring.common.SpringConfiguration;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PlayerTokenSpringConfiguration.class})
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
            SystemNotificationService notificationService,
            UsersConnectionRepository usersConnectionRepository,
            SystemNotificationServiceListener serviceListener) {
        SocialNetworkPopulatorEventListener networkPopulator = new SocialNetworkPopulatorEventListener(socialAdapterRegistry, usersConnectionRepository, notificationService);
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
    public ConnectionSignUp connectionSignUp() {
        return new PlayerSocialKeyGenerator();
    }

    @Bean
    public ServerProfileSocialRegistrationService serverProfileSocialRegistrationService(
        final ClembleCasinoValidationService validationService,
        final SocialConnectionAdapterRegistry socialAdapterRegistry,
        final SystemNotificationService notificationService,
        final SocialConnectionDataAdapter socialConnectionDataAdapter) {
        return new ServerProfileSocialRegistrationService(validationService, socialAdapterRegistry, notificationService, socialConnectionDataAdapter);
    }

    @Bean
    public PlayerSocialRegistrationController playerSocialRegistrationController(
        final PlayerTokenFactory playerTokenFactory,
        final ServerProfileSocialRegistrationService registrationService,
        final ClembleCasinoValidationService validationService,
        final SystemNotificationService notificationService) {
        return new PlayerSocialRegistrationController(
            playerTokenFactory,
            registrationService,
            validationService,
            notificationService);
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

    @Bean
    // If changing don't forget to change AuthenticationHandleInterceptor to filter out request for signin
    public ProviderSignInController providerSignInController(
        ConnectionFactoryLocator factoryLocator,
        UsersConnectionRepository usersConnectionRepository,
        SignInAdapter signInAdapter){
        return new ProviderSignInController(factoryLocator, usersConnectionRepository, signInAdapter);
    }

}
