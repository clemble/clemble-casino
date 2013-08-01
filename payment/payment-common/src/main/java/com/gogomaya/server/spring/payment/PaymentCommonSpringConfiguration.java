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
@Import({ PaymentCommonSpringConfiguration.Test.class, PaymentCommonSpringConfiguration.Default.class })
public class PaymentCommonSpringConfiguration implements SpringConfiguration {

    @Configuration
    @Import(ClientRestCommonSpringConfiguration.class)
    @Profile(SpringConfiguration.PROFILE_DEFAULT)
    public static class Default {

        @Bean
        @Autowired
        public PaymentTransactionService paymentTransactionService(RestTemplate restTemplate) {
            return new RestPaymentTransactionService("http://localhost:8080/payment-web", restTemplate);
        }

        @Bean
        @Autowired
        public PlayerAccountService playerAccountService(RestTemplate restTemplate) {
            return new RestPlayerAccountService("http://localhost:8080/payment-web", restTemplate);
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
