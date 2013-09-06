package com.gogomaya.server.spring.payment;

import java.util.Collection;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.money.Money;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.server.payment.PaymentTransactionServerService;
import com.gogomaya.server.payment.RestPaymentTransactionServerService;
import com.gogomaya.server.player.account.PlayerAccountServerService;
import com.gogomaya.server.player.account.RestPlayerAccountServerService;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.web.ClientRestCommonSpringConfiguration;

@Configuration
@Import({ PaymentCommonSpringConfiguration.Test.class,
        PaymentCommonSpringConfiguration.IntegrationCloudPaymentConfiguration.class,
        PaymentCommonSpringConfiguration.IntegrationTestPaymentConfiguration.class,
        PaymentCommonSpringConfiguration.IntegrationPaymentConfiguration.class })
public class PaymentCommonSpringConfiguration implements SpringConfiguration {

    @Configuration
    @Profile(value = SpringConfiguration.INTEGRATION_CLOUD)
    public static class IntegrationCloudPaymentConfiguration extends IntegrationPaymentConfiguration {

        @Override
        public String getBaseUrl() {
            return "http://ec2-50-16-93-157.compute-1.amazonaws.com/";
        }

    }

    @Configuration
    @Profile(value = { SpringConfiguration.INTEGRATION_TEST })
    public static class IntegrationTestPaymentConfiguration extends IntegrationPaymentConfiguration {

        @Override
        public String getBaseUrl() {
            return "http://localhost:9999/";
        }

    }

    @Import(ClientRestCommonSpringConfiguration.class)
    @Profile(value = { DEFAULT })
    public static class IntegrationPaymentConfiguration {

        @Autowired(required = false)
        @Qualifier("realPaymentTransactionService")
        public PaymentTransactionServerService realPaymentTransactionService;

        @Autowired(required = false)
        @Qualifier("realPlayerAccountService")
        public PlayerAccountServerService realPlayerAccountService;

        public String getBaseUrl() {
            return "http://localhost:8080/";
        }

        @Bean
        @Autowired
        public PaymentTransactionServerService paymentTransactionService(RestTemplate restTemplate) {
            return realPaymentTransactionService == null ? new RestPaymentTransactionServerService(getBaseUrl(), restTemplate) : realPaymentTransactionService;
        }

        @Bean
        @Autowired
        public PlayerAccountServerService playerAccountService(RestTemplate restTemplate) {
            return realPlayerAccountService == null ? new RestPlayerAccountServerService(getBaseUrl(), restTemplate) : realPlayerAccountService;
        }
    }

    @Configuration
    @Profile(SpringConfiguration.UNIT_TEST)
    public static class Test {

        @Bean
        @Singleton
        public PlayerAccountServerService playerAccountService() {
            return new PlayerAccountServerService() {

                @Override
                public PlayerAccount register(PlayerProfile playerProfile) {
                    return new PlayerAccount();
                }

                @Override
                public boolean canAfford(long playerId, Money amount) {
                    return true;
                }

                @Override
                public boolean canAfford(Collection<Long> playerId, Money amount) {
                    return true;
                }
            };
        }

        @Bean
        @Singleton
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
