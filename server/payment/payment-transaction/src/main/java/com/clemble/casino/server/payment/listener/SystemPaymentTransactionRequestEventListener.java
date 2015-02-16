package com.clemble.casino.server.payment.listener;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.event.PaymentCompleteEvent;
import com.clemble.casino.payment.event.PaymentEvent;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.payment.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.payment.repository.PendingTransactionRepository;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.payment.repository.PaymentTransactionRepository;
import com.clemble.casino.server.payment.repository.ServerAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mavarazy on 7/5/14.
 */
@Validated
public class SystemPaymentTransactionRequestEventListener implements SystemEventListener<SystemPaymentTransactionRequestEvent>{

    final private Logger LOG = LoggerFactory.getLogger(SystemPaymentTransactionRequestEventListener.class);

    final private ServerAccountService accountTemplate;

    public SystemPaymentTransactionRequestEventListener(ServerAccountService accountTemplate) {
        this.accountTemplate = checkNotNull(accountTemplate);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onEvent(@Valid SystemPaymentTransactionRequestEvent event) {
        PaymentTransaction paymentTransaction = event.getTransaction();
        LOG.debug("{} start", paymentTransaction.getTransactionKey());
        // Step 1. Sanity check
        if (paymentTransaction == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.PaymentTransactionEmpty, PlayerAware.DEFAULT_PLAYER, event.getTransaction().getTransactionKey());
        // Step 2. Processing payment transactions
        accountTemplate.process(paymentTransaction);
        LOG.debug("{} finish", paymentTransaction.getTransactionKey());
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
