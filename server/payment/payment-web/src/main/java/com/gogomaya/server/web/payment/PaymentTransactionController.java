package com.gogomaya.server.web.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PaymentTransactionId;
import com.gogomaya.payment.service.PaymentTransactionService;
import com.gogomaya.server.payment.PaymentTransactionServerService;
import com.gogomaya.server.repository.payment.PaymentTransactionRepository;
import com.gogomaya.web.mapping.WebMapping;
import com.gogomaya.web.payment.PaymentWebMapping;

@Controller
public class PaymentTransactionController implements PaymentTransactionService, PaymentTransactionServerService {

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
    public @ResponseBody PaymentTransaction getPaymentTransaction(
            @RequestHeader("playerId") long requesterId,
            @PathVariable("source") String source,
            @PathVariable("transactionId") long transactionId) {
        // Step 1. Checking payment transaction exists
        PaymentTransactionId paymentTransactionId = new PaymentTransactionId(source, transactionId);
        PaymentTransaction paymentTransaction = paymentTransactionRepository.findOne(paymentTransactionId);
        if (paymentTransaction == null)
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionNotExists);
        // Step 2. Checking player is one of the participants
        if (!paymentTransaction.isParticipant(requesterId))
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionAccessDenied);
        return paymentTransaction;
    }
    

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody List<PaymentTransaction> listPlayerTransaction(@PathVariable("playerId") long playerId) {
        // Step 1. Sending transactions
        return paymentTransactionRepository.findByPaymentOperationsPlayerId(playerId);
    }

}
