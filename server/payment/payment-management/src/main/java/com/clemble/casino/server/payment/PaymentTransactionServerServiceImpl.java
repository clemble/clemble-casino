package com.clemble.casino.server.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.event.FinishedPaymentEvent;
import com.clemble.casino.payment.event.PaymentEvent;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.server.repository.payment.PlayerAccountRepository;

public class PaymentTransactionServerServiceImpl implements PaymentTransactionServerService {

    final private PlayerAccountRepository playerAccountRepository;
    final private PlayerNotificationService notificationService;
    final private PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransactionServerServiceImpl(
            PaymentTransactionRepository paymentTransactionRepository,
            PlayerAccountRepository playerWalletRepository,
            PlayerNotificationService notificationService) {
        this.paymentTransactionRepository = checkNotNull(paymentTransactionRepository);
        this.playerAccountRepository = checkNotNull(playerWalletRepository);
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentTransaction process(PaymentTransaction paymentTransaction) {
        // Step 1. Sanity check
        if (paymentTransaction == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.PaymentTransactionEmpty);
        // Step 2. Processing payment transactions
        return processTransaction(paymentTransaction);
    }

    private PaymentTransaction processTransaction(PaymentTransaction paymentTransaction) {
        Collection<PaymentEvent> paymentEvents = new ArrayList<>();
        // Step 1. Processing payment transactions
        Collection<PlayerAccount> updatedAccounts = new ArrayList<>(paymentTransaction.getPaymentOperations().size());
        for (PaymentOperation paymentOperation : paymentTransaction.getPaymentOperations()) {
            PlayerAccount associatedWallet = playerAccountRepository.findOne(paymentOperation.getPlayer());
            if (associatedWallet == null) {
                throw ClembleCasinoException.fromError(ClembleCasinoError.PaymentTransactionUnknownPlayers);
            } else if (paymentOperation.getOperation() == Operation.Credit) {
                associatedWallet.subtract(paymentOperation.getAmount());
            } else if (paymentOperation.getOperation() == Operation.Debit) {
                associatedWallet.add(paymentOperation.getAmount());
            } else {
                throw ClembleCasinoException.fromError(ClembleCasinoError.PaymentTransactionInvalid);
            }
            updatedAccounts.add(associatedWallet);
            paymentEvents.add(new FinishedPaymentEvent(paymentTransaction.getTransactionKey(), paymentOperation));
        }
        // Step 2. Performing all account operations at a batch
        playerAccountRepository.save(updatedAccounts);
        playerAccountRepository.flush();
        // Step 3. Saving account transaction
        PaymentTransaction transaction = paymentTransactionRepository.save(paymentTransaction);
        // Step 4. Sending PaymentEvent notification
        notificationService.notify(paymentEvents);
        return transaction;

    }

    @Override
    public PaymentTransaction getTransaction(String source, String transactionId) {
        return paymentTransactionRepository.findOne(new PaymentTransactionKey(source, transactionId));
    }

    @Override
    public List<PaymentTransaction> getPlayerTransactions(String player) {
        return paymentTransactionRepository.findByPaymentOperationsPlayer(player);
    }

    @Override
    public List<PaymentTransaction> getPlayerTransactionsWithSource(String player, String source) {
        return paymentTransactionRepository.findByPaymentOperationsPlayerAndTransactionKeySourceLike(player, source + "%");
    }
}
