package com.clemble.casino.server.social.spring;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.server.security.PlayerTokenUtils;
import com.clemble.casino.server.social.*;
import com.clemble.casino.server.security.PlayerTokenFactory;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.PlayerTokenSpringConfiguration;
import com.clemble.casino.server.social.controller.PlayerSocialRegistrationController;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import org.eluder.spring.social.mongodb.MongoConnectionTransformers;
import org.eluder.spring.social.mongodb.MongoUsersConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.social.adapter.FacebookSocialAdapter;
import com.clemble.casino.server.social.adapter.LinkedInSocialAdapter;
import com.clemble.casino.server.social.adapter.TwitterSocialAdapter;
import com.clemble.casino.server.spring.common.SpringConfiguration;

import javax.annotation.PostConstruct;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PlayerTokenSpringConfiguration.class, PlayerSocialSpringConfiguration.SocialTunerSpringConfigurion.class, MongoSpringConfiguration.class})
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
    public PlayerSocialRegistrationController playerSocialRegistrationController(
        final PlayerTokenFactory playerTokenFactory,
        final SocialConnectionDataAdapter registrationService,
        final ClembleCasinoValidationService validationService,
        final SystemNotificationService notificationService) {
        return new PlayerSocialRegistrationController(
            playerTokenFactory,
            registrationService,
            validationService,
            notificationService);
    }

    @Bean
    public MongoConnectionTransformers mongoConnectionTransformers(ConnectionFactoryRegistry connectionFactoryLocator) {
        return new MongoConnectionTransformers(connectionFactoryLocator, Encryptors.noOpText());
    }

    @Bean
    public UsersConnectionRepository usersConnectionRepository(
//            @Qualifier("dataSource") DataSource dataSource,
            MongoOperations mongoOperations,
            MongoConnectionTransformers connectionTransformers,
            ConnectionSignUp connectionSignUp,
            ConnectionFactoryRegistry connectionFactoryLocator) {
        MongoUsersConnectionRepository repository = new MongoUsersConnectionRepository(mongoOperations, connectionFactoryLocator, connectionTransformers);
//        InMemoryUsersConnectionRepository repository = new InMemoryUsersConnectionRepository(connectionFactoryLocator);
//        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        repository.setConnectionSignUp(connectionSignUp);
        return repository;
    }

    @Bean
    public SocialSignInAdapter socialSignInAdapter(
        @Value("${clemble.registration.token.host}") String host,
        PlayerTokenUtils tokenUtils,
        SocialConnectionDataAdapter connectionDataAdapter){
        return new SocialSignInAdapter("http://" + host.substring(1), tokenUtils, connectionDataAdapter);
    }

    @Bean
    // If changing don't forget to change AuthenticationHandleInterceptor to filter out request for signin
    public ProviderSignInController providerSignInController(
        @Value("${clemble.registration.token.host}") String host,
        ConnectionFactoryLocator factoryLocator,
        UsersConnectionRepository usersConnectionRepository,
        SignInAdapter signInAdapter){
        ProviderSignInController signInController = new ProviderSignInController(factoryLocator, usersConnectionRepository, signInAdapter);
        return signInController;
    }

    @Bean
    public PlayerTokenUtils tokenUtils(
        @Value("${clemble.registration.token.host}") String host,
        @Value("${clemble.registration.token.maxAge}") int maxAge
    ){
        return new PlayerTokenUtils(host, maxAge);
    }

    @Configuration
    public static class SocialTunerSpringConfigurion implements SpringConfiguration {

        @Value("${clemble.registration.token.host}")
        public String host;

        @Autowired
        public ProviderSignInController signInController;

        @PostConstruct
        public void initialize() {
            signInController.setApplicationUrl("http://api" + host + "/social");
        }

    }

}
