package com.clemble.casino.server.spring.common;

import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.money.Currency;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.server.payment.RestPlayerAccountService;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by mavarazy on 7/5/14.
 */
@Configuration
@Import({ PaymentClientSpringConfiguration.Test.class, PaymentClientSpringConfiguration.Default.class })
public class PaymentClientSpringConfiguration implements SpringConfiguration {

    @Configuration
    @Profile(value = { TEST })
    public static class Test {

        @Autowired(required = false)
        @Qualifier("playerAccountController")
        public PlayerAccountService playerAccountController;

        @Bean
        public PlayerAccountService playerAccountClient(){
            if (playerAccountController != null)
                return playerAccountController;
            return new PlayerAccountService() {
                @Override
                public PlayerAccount myAccount() {
                    return null;
                }
                @Override
                public PlayerAccount getAccount(String playerWalletId) {
                    return null;
                }
                @Override
                public List<String> canAfford(Collection<String> players, Currency currency, Long amount) {
                    return Collections.emptyList();
                }
            };
        }
    }

    @Configuration
    @Profile(value = { DEFAULT, INTEGRATION_TEST, INTEGRATION_DEFAULT, CLOUD })
    public static class Default {

        @Autowired(required = false)
        @Qualifier("playerAccountController")
        public PlayerAccountService playerAccountController;

        @Bean
        public PlayerAccountService playerAccountClient(@Value("${clemble.host}") String base){
            if (playerAccountController != null)
                return playerAccountController;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
                @Override
                public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                    request.getHeaders().add("Cookie", "player=casino");
                    return execution.execute(request, body);
                }
            });
            return new RestPlayerAccountService(base, restTemplate);
        }
    }

}
