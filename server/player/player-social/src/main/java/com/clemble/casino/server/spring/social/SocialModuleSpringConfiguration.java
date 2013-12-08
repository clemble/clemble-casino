package com.clemble.casino.server.spring.social;

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

import com.clemble.casino.server.player.PlayerIdGenerator;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.social.SocialConnectionAdapterRegistry;
import com.clemble.casino.server.social.SocialConnectionDataAdapter;
import com.clemble.casino.server.social.SocialPlayerProfileCreator;
import com.clemble.casino.server.social.adapter.FacebookSocialAdapter;
import com.clemble.casino.server.spring.common.BasicJPASpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.player.PlayerManagementSpringConfiguration;

@Configuration
@Import(value = { PlayerManagementSpringConfiguration.class, BasicJPASpringConfiguration.class })
public class SocialModuleSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("dataSource")
    public DataSource dataSource;

    @Autowired
    @Qualifier("playerProfileRepository")
    public PlayerProfileRepository playerProfileRepository;

    @Bean
    @Autowired
    public SocialConnectionDataAdapter socialConnectionDataAdapter(UsersConnectionRepository usersConnectionRepository, SocialConnectionAdapterRegistry socialConnectionAdapterRegistry) {
        return new SocialConnectionDataAdapter(connectionFactoryLocator(), usersConnectionRepository, socialConnectionAdapterRegistry);
    }

    @Bean
    public ConnectionFactoryRegistry connectionFactoryLocator() {
        ConnectionFactoryRegistry connectionFactoryRegistry = new ConnectionFactoryRegistry();
        connectionFactoryRegistry.addConnectionFactory(facebookConnectionFactory());
        return connectionFactoryRegistry;
    }

    @Bean
    public FacebookConnectionFactory facebookConnectionFactory() {
        FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory("262763360540886", "beb651a120e8bf7252ba4e4be4f46437");
        return facebookConnectionFactory;
    }

    @Bean
    @Autowired
    public SocialConnectionAdapterRegistry socialAdapterRegistry(FacebookConnectionFactory facebookConnectionFactory) {
        SocialConnectionAdapterRegistry socialAdapterRegistry = new SocialConnectionAdapterRegistry();
        socialAdapterRegistry.register(new FacebookSocialAdapter(facebookConnectionFactory));
        return socialAdapterRegistry;
    }

    @Bean
    @Autowired
    public ConnectionSignUp connectionSignUp(PlayerIdGenerator idGenerator, PlayerProfileRepository profileRepository, SocialConnectionAdapterRegistry socialAdapterRegistry) {
        return new SocialPlayerProfileCreator(idGenerator, profileRepository, socialAdapterRegistry);
    }

    @Bean
    @Autowired
    public UsersConnectionRepository usersConnectionRepository(ConnectionSignUp connectionSignUp) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), Encryptors.noOpText());
        repository.setConnectionSignUp(connectionSignUp);
        return repository;
    }

}
