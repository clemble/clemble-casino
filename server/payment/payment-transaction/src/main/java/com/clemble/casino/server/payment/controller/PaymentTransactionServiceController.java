package com.clemble.casino.server.payment.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.payment.service.PaymentTransactionServiceBase;
import com.clemble.casino.server.ExternalController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.server.payment.repository.PaymentTransactionRepository;
import com.clemble.casino.web.mapping.WebMapping;
import static com.clemble.casino.payment.PaymentWebMapping.*;

@Controller
public class PaymentTransactionServiceController implements PaymentTransactionServiceBase, ExternalController {

    final private PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransactionServiceController(final PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = checkNotNull(paymentTransactionRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PAYMENT_TRANSACTIONS_TRANSACTION, produces = WebMapping.PRODUCES)
    public @ResponseBody PaymentTransaction getTransaction(
        @PathVariable("source") String source,
        @PathVariable("transaction") String transactionId
    ) {
        // Step 1. Checking payment transaction exists
        PaymentTransactionKey paymentTransactionId = new PaymentTransactionKey(source, transactionId);
        PaymentTransaction paymentTransaction = paymentTransactionRepository.findOne(paymentTransactionId);
//        TODO PaymentTransactionNotExists exception might be overrated
//        if (paymentTransaction == null)
//            throw ClembleCasinoException.fromError(ClembleCasinoError.PaymentTransactionNotExists);
        return paymentTransaction;
    }
    

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody List<PaymentTransaction> getPlayerTransactions(
        @PathVariable("player") String player
    ) {
        // Step 1. Sending transactions
        return paymentTransactionRepository.findByPaymentOperationsPlayer(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PAYMENT_ACCOUNTS_PLAYER_TRANSACTION_SOURCE, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody List<PaymentTransaction> getPlayerTransactionsWithSource(
        @PathVariable("player") String player,
        @PathVariable("source") String source
    ) {
        return paymentTransactionRepository.findByPaymentOperationsPlayerAndTransactionKeySourceLike(player, source);
    }

}
