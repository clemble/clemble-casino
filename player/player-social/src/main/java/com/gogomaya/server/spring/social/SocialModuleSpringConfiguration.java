package com.gogomaya.server.spring.social;

import javax.inject.Singleton;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import com.gogomaya.server.player.registration.PlayerRegistrationService;
import com.gogomaya.server.repository.player.PlayerIdentityRepository;
import com.gogomaya.server.repository.player.PlayerProfileRepository;
import com.gogomaya.server.social.SocialConnectionAdapterRegistry;
import com.gogomaya.server.social.SocialConnectionDataAdapter;
import com.gogomaya.server.social.SocialPlayerProfileCreator;
import com.gogomaya.server.social.adapter.FacebookSocialAdapter;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.player.PlayerManagementSpringConfiguration;

@Configuration
@Import(value = { PlayerManagementSpringConfiguration.class })
public class SocialModuleSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("dataSource")
    public DataSource dataSource;

    @Autowired
    @Qualifier("playerProfileRepository")
    public PlayerProfileRepository playerProfileRepository;

    @Autowired
    @Qualifier("playerIdentityRepository")
    public PlayerIdentityRepository playerIdentityRepository;

    @Autowired
    @Qualifier("playerRegistrationService")
    public PlayerRegistrationService playerRegistrationService;

    @Bean @Singleton
    public SocialConnectionDataAdapter socialConnectionDataAdapter() {
        return new SocialConnectionDataAdapter(connectionFactoryLocator(), usersConnectionRepository(), socialAdapterRegistry());
    }

    @Bean @Singleton
    public ConnectionFactoryRegistry connectionFactoryLocator() {
        ConnectionFactoryRegistry connectionFactoryRegistry = new ConnectionFactoryRegistry();
        connectionFactoryRegistry.addConnectionFactory(facebookConnectionFactory());
        return connectionFactoryRegistry;
    }

    @Bean @Singleton
    public FacebookConnectionFactory facebookConnectionFactory() {
        FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory("486714778051999", "cd4976a1ae74d3e70e804e1ae82b7eb6");
        return facebookConnectionFactory;
    }

    @Bean @Singleton
    public SocialConnectionAdapterRegistry socialAdapterRegistry() {
        SocialConnectionAdapterRegistry socialAdapterRegistry = new SocialConnectionAdapterRegistry();
        socialAdapterRegistry.register(new FacebookSocialAdapter());
        return socialAdapterRegistry;
    }

    @Bean @Singleton
    public ConnectionSignUp connectionSignUp() {
        return new SocialPlayerProfileCreator(playerRegistrationService, socialAdapterRegistry());
    }

    @Bean @Singleton
    public UsersConnectionRepository usersConnectionRepository() {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), Encryptors.noOpText());
        repository.setConnectionSignUp(connectionSignUp());
        return repository;
    }

}
