package com.clemble.casino.server.social.spring;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.server.security.PlayerTokenUtils;
import com.clemble.casino.server.social.*;
import com.clemble.casino.server.security.PlayerTokenFactory;
import com.clemble.casino.server.social.adapter.*;
import com.clemble.casino.server.social.connection.ClembleUsersConnectionRepository;
import com.clemble.casino.server.social.listener.SocialNetworkPopulatorEventListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.PlayerTokenSpringConfiguration;
import com.clemble.casino.server.social.controller.PlayerSocialRegistrationController;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import org.eluder.spring.social.mongodb.MongoConnectionTransformers;
import org.eluder.spring.social.mongodb.MongoUsersConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.social.vkontakte.connect.VKontakteConnectionFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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
            GoogleConnectionFactory googleConnectionFactory,
            LinkedInConnectionFactory linkedInConnectionFactory,
            TwitterConnectionFactory twitterConnectionFactory,
            VKontakteConnectionFactory vKontakteConnectionFactory) {
        ConnectionFactoryRegistry connectionFactoryRegistry = new ConnectionFactoryRegistry();
        // Step 1.1 Registering FB
        connectionFactoryRegistry.addConnectionFactory(facebookConnectionFactory);
        socialConnectionAdapterRegistry.register(new FacebookSocialAdapter(facebookConnectionFactory));
        // Step 1.2. Registering Twitter
        connectionFactoryRegistry.addConnectionFactory(twitterConnectionFactory);
        socialConnectionAdapterRegistry.register(new TwitterSocialAdapter(twitterConnectionFactory));
        // Step 1.3. Register VK
        connectionFactoryRegistry.addConnectionFactory(vKontakteConnectionFactory);
        socialConnectionAdapterRegistry.register(new VKontakteSocialAdapter(vKontakteConnectionFactory));
        // Step 1.4 Registering LinkedIn
        connectionFactoryRegistry.addConnectionFactory(linkedInConnectionFactory);
        socialConnectionAdapterRegistry.register(new LinkedInSocialAdapter(linkedInConnectionFactory));
        // Step 1.5 Registering Google
        connectionFactoryRegistry.addConnectionFactory(googleConnectionFactory);
        socialConnectionAdapterRegistry.register(new GoogleSocialAdapter(googleConnectionFactory));
        // Step 2. Returning connection registry
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
    public VKontakteConnectionFactory vKontakteConnectionFactory(
        @Value("${clemble.social.vkontakte.key}") String key,
        @Value("${clemble.social.vkontakte.secret}") String secret) {
        return new VKontakteConnectionFactory(key, secret);
    }

    @Bean
    public GoogleConnectionFactory googleConnectionFactory(
        @Value("${clemble.social.google.key}") String key,
        @Value("${clemble.social.google.secret}") String secret) {
        return new GoogleConnectionFactory(key, secret);
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
            MongoOperations mongoOperations,
            MongoConnectionTransformers connectionTransformers,
            ConnectionSignUp connectionSignUp,
            ConnectionFactoryRegistry connectionFactoryLocator,
            SystemNotificationService systemNotificationService) {
        MongoUsersConnectionRepository repository = new MongoUsersConnectionRepository(mongoOperations, connectionFactoryLocator, connectionTransformers);
        repository.setConnectionSignUp(connectionSignUp);
        return new ClembleUsersConnectionRepository(repository, systemNotificationService);
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
        ConnectionFactoryLocator factoryLocator,
        UsersConnectionRepository usersConnectionRepository,
        SignInAdapter signInAdapter){
        ProviderSignInController signInController = new ProviderSignInController(factoryLocator, usersConnectionRepository, signInAdapter);
        return signInController;
    }

    @Bean
    @Scope(value="request", proxyMode= ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository(UsersConnectionRepository connectionRepository, HttpServletRequest request) {
        // Step 1. Fetching player TODO update when you'll be handling security
        String player = null;
        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("player")) {
                    player = cookie.getValue();
                }
            }
        }
        // Step 2. Getting connection repository
        return connectionRepository.createConnectionRepository(player);
    }


    @Bean
    public ConnectController connectController(
        ConnectionFactoryLocator connectionFactoryLocator,
        ConnectionRepository connectionRepository,
        @Value("${clemble.registration.token.host}") String host){
        final String redirect = "http://" + host.substring(1);
        ConnectController connectController = new ConnectController(connectionFactoryLocator, connectionRepository){

            protected RedirectView connectionStatusRedirect(String providerId, NativeWebRequest request) {
                return new RedirectView(redirect, true);
            }

        }
        return connectController;
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

        @Autowired
        public ConnectController connectController;

        @PostConstruct
        public void initialize() {
            String url = "http://api" + host + "/social";
            signInController.setApplicationUrl(url);
            connectController.setApplicationUrl(url);
        }

    }

}
