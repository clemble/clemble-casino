package com.clemble.casino.server.spring.common;

import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.connection.RESTPlayerConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * Created by mavarazy on 11/9/14.
 */
@Configuration
@Import({ ConnectionClientSpringConfiguration.Test.class, ConnectionClientSpringConfiguration.Default.class })
public class ConnectionClientSpringConfiguration implements SpringConfiguration{

    @Configuration
    @Profile(value = { TEST })
    public static class Test {

        @Autowired(required = false)
        @Qualifier("playerConnectionController")
        public PlayerConnectionService playerConnectionController;

        @Bean
        public PlayerConnectionService playerConnectionClient(){
            if (playerConnectionController != null)
                return playerConnectionController;
            return new PlayerConnectionService() {
                @Override
                public Set<String> getConnections(String player) {
                    return Collections.emptySet();
                }
                @Override
                public Set<String> myConnections() {
                    return Collections.emptySet();
                }
            };
        }
    }

    @Configuration
    @Profile(value = { DEFAULT, INTEGRATION_TEST, INTEGRATION_DEFAULT, CLOUD })
    public static class Default {

        @Autowired(required = false)
        @Qualifier("playerConnectionController")
        public PlayerConnectionService playerConnectionController;

        @Bean
        public PlayerConnectionService playerConnectionClient(@Value("${clemble.host}") String base){
            if (playerConnectionController != null)
                return playerConnectionController;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
                @Override
                public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                    request.getHeaders().add("Cookie", "player=casino");
                    return execution.execute(request, body);
                }
            });
            return new RESTPlayerConnectionService(base, restTemplate);
        }
    }

}
