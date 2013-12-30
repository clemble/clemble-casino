package com.clemble.casino.server.spring.social;

import com.clemble.casino.server.player.PlayerIdGenerator;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;
import com.clemble.casino.server.social.SocialConnectionAdapterRegistry;
import com.clemble.casino.server.social.SocialConnectionDataAdapter;
import com.clemble.casino.server.social.SocialPlayerProfileCreator;
import com.clemble.casino.server.social.adapter.FacebookSocialAdapter;
import com.clemble.casino.server.social.adapter.LinkedInSocialAdapter;
import com.clemble.casino.server.social.adapter.TwitterSocialAdapter;
import com.clemble.casino.server.spring.common.BasicJPASpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.player.PlayerManagementSpringConfiguration;

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
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import javax.sql.DataSource;

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
    public SocialConnectionDataAdapter socialConnectionDataAdapter(UsersConnectionRepository usersConnectionRepository, SocialConnectionAdapterRegistry socialConnectionAdapterRegistry, ConnectionFactoryRegistry connectionFactoryLocator) {
        return new SocialConnectionDataAdapter(connectionFactoryLocator, usersConnectionRepository, socialConnectionAdapterRegistry);
    }

    @Bean
    @Autowired
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
    public FacebookConnectionFactory facebookConnectionFactory() {
        FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory("262763360540886", "beb651a120e8bf7252ba4e4be4f46437");
        return facebookConnectionFactory;
    }

    @Bean
    public TwitterConnectionFactory twitterConnectionFactory() {
        return new TwitterConnectionFactory("6TV1yY2JeICz3cbX1m9Pnw", "Qig0Ix1gC9W0m77rTWH8CnE9FYfenmiP3GGk4hGlEo");
    }

    @Bean
    public LinkedInConnectionFactory linkedInConnectionFactory() {
        return new LinkedInConnectionFactory("777wwpeqpwl4u1", "PpqvRnoACnPxXNY9");
    }

    @Bean
    public SocialConnectionAdapterRegistry socialAdapterRegistry() {
        return new SocialConnectionAdapterRegistry();
    }

    @Bean
    @Autowired
    public ConnectionSignUp connectionSignUp(PlayerIdGenerator idGenerator, PlayerProfileRepository profileRepository, SocialConnectionAdapterRegistry socialAdapterRegistry, PlayerSocialNetworkRepository socialNetworkRepository) {
        return new SocialPlayerProfileCreator(idGenerator, profileRepository, socialAdapterRegistry, socialNetworkRepository);
    }

    @Bean
    @Autowired
    public UsersConnectionRepository usersConnectionRepository(ConnectionSignUp connectionSignUp, ConnectionFactoryRegistry connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        repository.setConnectionSignUp(connectionSignUp);
        return repository;
    }

}
