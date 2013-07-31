package com.gogomaya.server.spring.payment;

import java.util.Collection;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.gogomaya.server.money.Money;
import com.gogomaya.server.payment.PaymentOperation;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.payment.PaymentTransactionService;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.account.PlayerAccount;
import com.gogomaya.server.player.account.PlayerAccountService;
import com.gogomaya.server.spring.common.SpringConfiguration;

@Configuration
@Import(PaymentCommonSpringConfiguration.Test.class)
public class PaymentCommonSpringConfiguration implements SpringConfiguration {

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
                public PaymentTransaction process(PaymentTransaction paymentTransaction) {
                    return paymentTransaction;
                }
            };
        }

    }
}
