package com.gogomaya.server.spring.payment;

import java.util.Collection;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.money.Money;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.payment.PaymentTransactionService;
import com.gogomaya.server.payment.RestPaymentTransactionService;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.account.PlayerAccount;
import com.gogomaya.server.player.account.PlayerAccountService;
import com.gogomaya.server.player.account.RestPlayerAccountService;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.web.ClientRestCommonSpringConfiguration;

@Configuration
@Import({ PaymentCommonSpringConfiguration.Test.class, PaymentCommonSpringConfiguration.RemoteIntegrationPaymentConfiguration.class,
        PaymentCommonSpringConfiguration.LocalIntegrationPaymentConfiguration.class,
        PaymentCommonSpringConfiguration.DefaultIntegrationPaymentConfiguration.class })
public class PaymentCommonSpringConfiguration implements SpringConfiguration {

    @Configuration
    @Profile(value = SpringConfiguration.PROFILE_INTEGRATION_CLOUD)
    public static class RemoteIntegrationPaymentConfiguration extends IntegrationPaymentConfiguration {

        @Override
        public String getBaseUrl() {
            return "http://ec2-50-16-93-157.compute-1.amazonaws.com/gogomaya/";
        }

    }

    @Configuration
    @Profile(value = { SpringConfiguration.PROFILE_INTEGRATION_LOCAL_TEST, SpringConfiguration.PROFILE_DEFAULT })
    public static class LocalIntegrationPaymentConfiguration extends IntegrationPaymentConfiguration {

        @Override
        public String getBaseUrl() {
            return "http://localhost:9999/";
        }

    }

    @Configuration
    @Profile(value = { SpringConfiguration.PROFILE_INTEGRATION_LOCAL_SERVER })
    public static class DefaultIntegrationPaymentConfiguration extends IntegrationPaymentConfiguration {
    }

    @Import(ClientRestCommonSpringConfiguration.class)
    public static class IntegrationPaymentConfiguration {

        public String getBaseUrl() {
            return "http://localhost:8080/";
        }

        @Bean
        @Autowired
        public PaymentTransactionService paymentTransactionService(RestTemplate restTemplate) {
            return new RestPaymentTransactionService(getBaseUrl(), restTemplate);
        }

        @Bean
        @Autowired
        public PlayerAccountService playerAccountService(RestTemplate restTemplate) {
            return new RestPlayerAccountService(getBaseUrl(), restTemplate);
        }
    }

    @Configuration
    @Profile(SpringConfiguration.PROFILE_TEST)
    public static class Test {

        @Bean
        @Singleton
        public PlayerAccountService playerAccountService() {
            return new PlayerAccountService() {

                @Override
                public PlayerAccount register(PlayerProfile playerProfile) {
                    return new PlayerAccount();
                }

                @Override
                public boolean canAfford(long playerId, Money ammount) {
                    return true;
                }

                @Override
                public boolean canAfford(Collection<Long> playerId, Money ammount) {
                    return true;
                }
            };
        }

        @Bean
        @Singleton
        public PaymentTransactionService paymentTransactionService() {
            return new PaymentTransactionService() {

                @Override
                public PaymentTransaction process(PaymentTransaction paymentTransaction) {
                    return paymentTransaction;
                }
            };
        }

    }
}
