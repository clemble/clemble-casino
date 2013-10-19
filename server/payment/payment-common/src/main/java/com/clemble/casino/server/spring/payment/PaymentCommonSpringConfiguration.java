package com.clemble.casino.server.spring.payment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.configuration.ServerRegistryServerService;
import com.clemble.casino.server.payment.PaymentTransactionServerService;
import com.clemble.casino.server.payment.RestPaymentTransactionServerService;
import com.clemble.casino.server.player.account.PlayerAccountServerService;
import com.clemble.casino.server.player.account.RestPlayerAccountServerService;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.ServerRegistrySpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.web.ClientRestCommonSpringConfiguration;

@Configuration
@Import({ CommonSpringConfiguration.class, PaymentCommonSpringConfiguration.Test.class, PaymentCommonSpringConfiguration.Integration.class })
public class PaymentCommonSpringConfiguration implements SpringConfiguration {

    @Configuration
    @Import({ ClientRestCommonSpringConfiguration.class, ServerRegistrySpringConfiguration.class })
    @Profile(value = { DEFAULT, INTEGRATION_CLOUD, CLOUD, INTEGRATION_TEST })
    public static class Integration {

        @Autowired
        public ServerRegistryServerService serverRegistryService;

        @Autowired
        public RestTemplate restTemplate;

        @Autowired(required = false)
        @Qualifier("realPaymentTransactionService")
        public PaymentTransactionServerService realPaymentTransactionService;

        @Autowired(required = false)
        @Qualifier("realPlayerAccountService")
        public PlayerAccountServerService realPlayerAccountService;

        @Bean
        @Autowired
        public PaymentTransactionServerService paymentTransactionService() {
            return realPaymentTransactionService == null ? new RestPaymentTransactionServerService(serverRegistryService, restTemplate)
                    : realPaymentTransactionService;
        }

        @Bean
        @Autowired
        public PlayerAccountServerService playerAccountService() {
            return realPlayerAccountService == null ? new RestPlayerAccountServerService(serverRegistryService, restTemplate) : realPlayerAccountService;
        }
    }

    @Configuration
    @Profile(SpringConfiguration.UNIT_TEST)
    public static class Test {

        @Bean
        public PlayerAccountServerService playerAccountService() {
            return new PlayerAccountServerService() {

                @Override
                public PlayerAccount register(PlayerProfile playerProfile) {
                    return new PlayerAccount();
                }

                @Override
                public boolean canAfford(String playerId, Money amount) {
                    return true;
                }

                @Override
                public boolean canAfford(Collection<String> playerId, Money amount) {
                    return true;
                }
            };
        }

        @Bean
        public PaymentTransactionServerService paymentTransactionService() {
            return new PaymentTransactionServerService() {

                @Override
                public PaymentTransaction process(PaymentTransaction paymentTransaction) {
                    return paymentTransaction;
                }
            };
        }

    }
}
