package com.clemble.casino.server.spring.payment;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.DNSBasedServerRegistry;
import com.clemble.casino.ServerRegistry;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.payment.RestServerPaymentTransactionService;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;
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

        @Autowired
        public RestTemplate restTemplate;

        @Autowired(required = false)
        @Qualifier("realPaymentTransactionService")
        public ServerPaymentTransactionService realPaymentTransactionService;

        @Autowired(required = false)
        @Qualifier("realPlayerAccountService")
        public ServerPlayerAccountService realPlayerAccountService;

        @Bean
        public ServerPaymentTransactionService paymentTransactionService() {
            ServerRegistry paymentRegistry = new DNSBasedServerRegistry(0, "http://127.0.0.1:8080/payment/", "http://127.0.0.1:8080/payment/",
                    "http://127.0.0.1:8080/payment/");
            return realPaymentTransactionService == null ? new RestServerPaymentTransactionService(paymentRegistry, restTemplate)
                    : realPaymentTransactionService;
        }

        @Bean
        public ServerPlayerAccountService playerAccountService() {
            ServerRegistry paymentRegistry = new DNSBasedServerRegistry(0, "http://127.0.0.1:8080/payment/", "http://127.0.0.1:8080/payment/",
                    "http://127.0.0.1:8080/payment/");
            return realPlayerAccountService == null ? new RestServerPlayerAccountService(paymentRegistry, restTemplate) : realPlayerAccountService;
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
                public boolean canAfford(Collection<String> playerId, Money amount) {
                    return true;
                }
            };
        }

        @Bean
        public ServerPaymentTransactionService paymentTransactionService() {
            return new ServerPaymentTransactionService() {

                @Override
                public PaymentTransaction process(PaymentTransaction paymentTransaction) {
                    return paymentTransaction;
                }

                @Override
                public PaymentTransaction getTransaction(String source, String transactionId) {
                    return null;
                }

                @Override
                public List<PaymentTransaction> getPlayerTransactions(String player) {
                    return null;
                }

                @Override
                public List<PaymentTransaction> getPlayerTransactionsWithSource(String player, String source) {
                    return null;
                }
            };
        }

    }
}
