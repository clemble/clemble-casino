package com.gogomaya.server.spring.social;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import com.gogomaya.server.player.PlayerProfileRepository;
import com.gogomaya.server.social.SocialConnectionAdapterRegistry;
import com.gogomaya.server.social.SocialConnectionDataAdapter;
import com.gogomaya.server.social.SocialGamerProfileCreator;
import com.gogomaya.server.social.adapter.FacebookSocialAdapter;
import com.gogomaya.server.spring.player.PlayerManagementSpringConfiguration;

@Configuration
@Import(value = { PlayerManagementSpringConfiguration.class })
public class SocialModuleSpringConfiguration {

    @Inject
    DataSource dataSource;

    @Inject
    PlayerProfileRepository gamerProfileRepository;

    @Bean
    @Singleton
    public SocialConnectionDataAdapter socialConnectionDataAdapter() {
        return new SocialConnectionDataAdapter(connectionFactoryLocator(), usersConnectionRepository(), gamerProfileRepository, socialAdapterRegistry());
    }

    @Bean
    @Singleton
    public ConnectionFactoryRegistry connectionFactoryLocator() {
        ConnectionFactoryRegistry connectionFactoryRegistry = new ConnectionFactoryRegistry();
        connectionFactoryRegistry.addConnectionFactory(facebookConnectionFactory());
        return connectionFactoryRegistry;
    }

    @Bean
    @Singleton
    public FacebookConnectionFactory facebookConnectionFactory() {
        FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory("486714778051999", "cd4976a1ae74d3e70e804e1ae82b7eb6");
        return facebookConnectionFactory;
    }

    @Bean
    @Singleton
    public SocialConnectionAdapterRegistry socialAdapterRegistry() {
        SocialConnectionAdapterRegistry socialAdapterRegistry = new SocialConnectionAdapterRegistry();
        socialAdapterRegistry.register(new FacebookSocialAdapter());
        return socialAdapterRegistry;
    }

    @Bean
    @Singleton
    public ConnectionSignUp connectionSignUp() {
        return new SocialGamerProfileCreator(gamerProfileRepository, socialAdapterRegistry());
    }

    @Bean
    @Singleton
    public UsersConnectionRepository usersConnectionRepository() {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), Encryptors.noOpText());
        repository.setConnectionSignUp(connectionSignUp());
        return repository;
    }

}
