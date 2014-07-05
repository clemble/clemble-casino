package com.clemble.casino.server.spring.payment;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.DNSBasedServerRegistry;
import com.clemble.casino.ServerRegistry;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.player.account.RestServerPlayerAccountService;
import com.clemble.casino.server.player.account.ServerPlayerAccountService;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.web.ClientRestCommonSpringConfiguration;

@Configuration
@Import({ CommonSpringConfiguration.class, PaymentCommonSpringConfiguration.Test.class, PaymentCommonSpringConfiguration.Integration.class })
public class PaymentCommonSpringConfiguration implements SpringConfiguration {

    @Configuration
    @Import({ ClientRestCommonSpringConfiguration.class })
    @Profile(value = { DEFAULT, INTEGRATION_CLOUD, CLOUD, INTEGRATION_TEST })
    public static class Integration {

        @Autowired(required = false)
        @Qualifier("realPlayerAccountService")
        public ServerPlayerAccountService realPlayerAccountService;

        @Bean
        public ServerRegistry paymentServerRegistry(@Value("${clemble.service.payment.host}") String host) {
            return new DNSBasedServerRegistry(host);
        }

        @Bean
        public ServerPlayerAccountService playerAccountService(RestTemplate restTemplate, ServerRegistry paymentServerRegistry) {
            return realPlayerAccountService == null ? new RestServerPlayerAccountService(paymentServerRegistry, restTemplate) : realPlayerAccountService;
        }

    }

    @Configuration
    @Profile(SpringConfiguration.TEST)
    public static class Test {

        @Bean
        public ServerPlayerAccountService playerAccountService() {
            return new ServerPlayerAccountService() {

                @Override
                public boolean canAfford(String playerId, Money amount) {
                    return true;
                }

                @Override
                public List<String> canAfford(Collection<String> playerId, Money amount) {
                    return Collections.emptyList();
                }
            };
        }

    }
}
