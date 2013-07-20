package com.gogomaya.server.web.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.payment.PaymentTransactionId;
import com.gogomaya.server.payment.PaymentTransactionService;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.repository.payment.PaymentTransactionRepository;
import com.gogomaya.server.web.mapping.PaymentWebMapping;

@Controller
public class PaymentTransactionController {

    final private PaymentTransactionService paymentTransactionService;
    final private PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransactionController(final PaymentTransactionRepository paymentTransactionRepository,
            final PaymentTransactionService paymentTransactionService) {
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
        this.paymentTransactionRepository = checkNotNull(paymentTransactionRepository);
    }

    @RequestMapping(method = RequestMethod.POST, value = PaymentWebMapping.PAYMENT_ACCOUNTS, produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PaymentTransaction register(@RequestBody PlayerProfile playerProfile) {
        return paymentTransactionService.register(playerProfile);
    }

    @RequestMapping(method = RequestMethod.POST, value = PaymentWebMapping.PAYMENT_TRANSACTIONS, produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PaymentTransaction perform(@RequestBody PaymentTransaction paymentTransaction) {
        return paymentTransactionService.process(paymentTransaction);
    }

    @RequestMapping(method = RequestMethod.GET, value = PaymentWebMapping.PAYMENT_TRANSACTIONS_TRANSACTION, produces = "application/json")
    public @ResponseBody
    PaymentTransaction get(@RequestHeader("playerId") long requesterId, @PathVariable("source") String source, @PathVariable("transactionId") long transactionId) {
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

}
