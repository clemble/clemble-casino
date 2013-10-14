package com.clemble.casino.server.spring.web.management;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.clemble.casino.server.spring.common.JPASpringConfiguration;

@Configuration
@Import(value = { JPASpringConfiguration.class })
public class OAuth2ServerSpringConfiguration {

    @Autowired
    public DataSource dataSource;

    @Bean
    public ClientDetailsService clientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setClientDetailsService(clientDetailsService());
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Bean
    public AuthorizationEndpoint authorizationEndpoint() {
        AuthorizationEndpoint authorizationEndpoint = new AuthorizationEndpoint();
        authorizationEndpoint.setTokenGranter(new ImplicitTokenGranter(authorizationServerTokenServices(), clientDetailsService()));
        authorizationEndpoint.setClientDetailsService(clientDetailsService());
        return authorizationEndpoint;
    }

    @Bean
    public TokenEndpoint tokenEndpoint() {
        TokenEndpoint tokenEndpoint = new TokenEndpoint();
        tokenEndpoint.setTokenGranter(new RefreshTokenGranter(authorizationServerTokenServices(), clientDetailsService()));
        tokenEndpoint.setClientDetailsService(clientDetailsService());
        return tokenEndpoint;
    }

}
