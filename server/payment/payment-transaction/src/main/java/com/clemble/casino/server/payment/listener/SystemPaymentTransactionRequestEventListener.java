package com.clemble.casino.server.payment.listener;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.event.PaymentCompleteEvent;
import com.clemble.casino.payment.event.PaymentEvent;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.payment.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.payment.repository.PendingTransactionRepository;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.payment.repository.PaymentTransactionRepository;
import com.clemble.casino.server.payment.repository.PlayerAccountTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mavarazy on 7/5/14.
 */
public class SystemPaymentTransactionRequestEventListener implements SystemEventListener<SystemPaymentTransactionRequestEvent>{

    final private Logger LOG = LoggerFactory.getLogger(SystemPaymentTransactionRequestEventListener.class);

    final private PlayerAccountTemplate accountTemplate;
    final private PendingTransactionRepository pendingTransactionRepository;
    final private ServerNotificationService notificationService;
    final private ClembleCasinoValidationService validationService;
    final private PaymentTransactionRepository paymentTransactionRepository;

    public SystemPaymentTransactionRequestEventListener(
        PaymentTransactionRepository paymentTransactionRepository,
        PendingTransactionRepository pendingTransactionRepository,
        PlayerAccountTemplate accountTemplate,
        ServerNotificationService notificationService,
        ClembleCasinoValidationService validationService) {
        this.paymentTransactionRepository = checkNotNull(paymentTransactionRepository);
        this.pendingTransactionRepository = checkNotNull(pendingTransactionRepository);
        this.accountTemplate = checkNotNull(accountTemplate);
        this.notificationService = checkNotNull(notificationService);
        this.validationService = validationService;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onEvent(SystemPaymentTransactionRequestEvent event) {
        PaymentTransaction paymentTransaction = event.getTransaction();
        LOG.debug("{} start", paymentTransaction.getTransactionKey());
        // Step 1. Sanity check
        if (paymentTransaction == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.PaymentTransactionEmpty, PlayerAware.DEFAULT_PLAYER, event.getTransaction().getTransactionKey());
        // Step 2. Processing payment transactions
        validationService.validate(paymentTransaction);
        processTransaction(paymentTransaction);
        LOG.debug("{} finish", paymentTransaction.getTransactionKey());
    }

    private PaymentTransaction processTransaction(PaymentTransaction paymentTransaction) {
        // Step 0. Sanity check
        if(paymentTransactionRepository.exists(paymentTransaction.getTransactionKey())) {
            LOG.error("Payment transaction already exists {}", paymentTransaction);
            // TODO maybe you need to throw exception here
            return paymentTransactionRepository.findOne(paymentTransaction.getTransactionKey());
        }
        Collection<PaymentEvent> paymentEvents = new ArrayList<PaymentEvent>();
        // Step 1. Processing payment transactions
        for (PaymentOperation paymentOperation : paymentTransaction.getOperations()) {
            LOG.debug("Processing {}", paymentOperation);
            accountTemplate.process(paymentTransaction.getTransactionKey(), paymentOperation);
            paymentEvents.add(new PaymentCompleteEvent(paymentTransaction.getTransactionKey(), paymentOperation));
        }
        // Step 3. Saving account transaction
        PaymentTransaction transaction = paymentTransactionRepository.save(paymentTransaction);
        // Step 4. Sending PaymentEvent notification
        notificationService.send(paymentEvents);
        // Step 5. Removing pending transaction
        // TODO Add transaction verification
        pendingTransactionRepository.delete(paymentTransaction.getTransactionKey());
        return transaction;
    }

    @Override
    public String getChannel() {
        return SystemPaymentTransactionRequestEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPaymentTransactionRequestEvent.CHANNEL + " > payment:transaction:processor";
    }
}
