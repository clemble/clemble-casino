package com.clemble.casino.integration.payment;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.integration.player.account.CombinedPaymentService;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.payment.service.PaymentTransactionService;
import com.clemble.casino.payment.service.PlayerAccountService;

abstract public class PaymentServiceFactory {

    abstract public PaymentService construct(Player player);

    public static class SingletonPaymentService extends PaymentServiceFactory {

        final private PaymentTransactionService paymentTransactionService;
        final private PlayerAccountService playerAccountService;

        public SingletonPaymentService(PaymentTransactionService paymentTransactionService, PlayerAccountService playerAccountService) {
            this.paymentTransactionService = paymentTransactionService;
            this.playerAccountService = playerAccountService;
        }

        @Override
        public PaymentService construct(Player player) {
            return new CombinedPaymentService(paymentTransactionService, playerAccountService);
        }

    }

    public static class IntegrationPaymentServiceFactory extends PaymentServiceFactory {

        final private RestTemplate restTemplate;

        public IntegrationPaymentServiceFactory(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @Override
        public PaymentService construct(Player player) {
            return new IntegrationPaymentService(player, restTemplate);
        }

    }
}
