package com.clemble.casino.server.spring;

import com.clemble.casino.server.security.AESPlayerTokenFactory;
import com.clemble.casino.server.security.PlayerTokenFactory;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.NoSuchAlgorithmException;

/**
 * Created by mavarazy on 7/5/14.
 */
@Configuration
public class PlayerTokenSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerTokenFactory playerTokenFactory() throws NoSuchAlgorithmException {
        return new AESPlayerTokenFactory();
    }

}
