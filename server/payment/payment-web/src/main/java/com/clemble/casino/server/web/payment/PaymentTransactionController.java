package com.clemble.casino.server.web.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.server.payment.PaymentTransactionServerService;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.web.mapping.WebMapping;
import com.clemble.casino.web.payment.PaymentWebMapping;

@Controller
public class PaymentTransactionController implements PaymentTransactionServerService {

    final private PaymentTransactionServerService paymentTransactionService;
    final private PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransactionController(final PaymentTransactionRepository paymentTransactionRepository,
            final PaymentTransactionServerService paymentTransactionService) {
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
        this.paymentTransactionRepository = checkNotNull(paymentTransactionRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = PaymentWebMapping.PAYMENT_TRANSACTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody PaymentTransaction process(@RequestBody PaymentTransaction paymentTransaction) {
        return paymentTransactionService.process(paymentTransaction);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.PAYMENT_TRANSACTIONS_TRANSACTION, produces = WebMapping.PRODUCES)
    public @ResponseBody PaymentTransaction getTransaction(@PathVariable("source") String source,
            @PathVariable("transaction") String transactionId) {
        // Step 1. Checking payment transaction exists
        PaymentTransactionKey paymentTransactionId = new PaymentTransactionKey(source, transactionId);
        PaymentTransaction paymentTransaction = paymentTransactionRepository.findOne(paymentTransactionId);
        if (paymentTransaction == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.PaymentTransactionNotExists);
        return paymentTransaction;
    }
    

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody List<PaymentTransaction> getPlayerTransactions(@PathVariable("player") String player) {
        // Step 1. Sending transactions
        return paymentTransactionRepository.findByPaymentOperationsPlayer(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER_TRANSACTION_SOURCE, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<PaymentTransaction> getPlayerTransactionsWithSource(@PathVariable("player") String player, @PathVariable("source") String source) {
        return paymentTransactionService.getPlayerTransactionsWithSource(player, source);
    }

}
