package com.gogomaya.server.spring.payment;

import java.util.Collection;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.gogomaya.server.money.Money;
import com.gogomaya.server.payment.PaymentOperation;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.payment.PaymentTransactionService;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.account.PlayerAccountService;
import com.gogomaya.server.spring.common.SpringConfiguration;

@Configuration
public class CommonPaymentSpringConfiguration implements SpringConfiguration {

    @Configuration
    @Profile(SpringConfiguration.PROFILE_TEST)
    public static class Test {

        @Bean
        @Singleton
        public PlayerAccountService playerWalletService() {
            return new PlayerAccountService() {
                @Override
                public boolean canAfford(PaymentOperation paymentOperation) {
                    return true;
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
                public PaymentTransaction register(PlayerProfile playerProfile) {
                    return new PaymentTransaction();
                }

                @Override
                public PaymentTransaction process(PaymentTransaction paymentTransaction) {
                    return paymentTransaction;
                }
            };
        }

    }
}
