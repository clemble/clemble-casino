package com.gogomaya.server.spring.integration;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.integration.game.GameOperations;
import com.gogomaya.server.integration.game.RestGameOperations;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.integration.player.RestPlayerOperations;

@Configuration
public class IntegrationTestConfiguration {

    String baseUrl = "http://localhost:8080/gogomaya-web";
    //String baseUrl = "http://gogomaya.cloudfoundry.com/";
    
    @Bean
    @Singleton
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Singleton
    public PlayerOperations playerOperations() {
        return new RestPlayerOperations(baseUrl, restTemplate());
    }

    @Bean
    @Singleton
    public GameOperations gameOperations() {
        return new RestGameOperations(baseUrl, restTemplate());
    }

}
