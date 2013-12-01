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
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.server.payment.PaymentTransactionServerService;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.server.repository.payment.PlayerAccountRepository;

public class PaymentTransactionServerServiceImpl implements PaymentTransactionServerService {

    final private PlayerAccountRepository playerAccountRepository;
    final private PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransactionServerServiceImpl(PaymentTransactionRepository paymentTransactionRepository, PlayerAccountRepository playerWalletRepository) {
        this.paymentTransactionRepository = checkNotNull(paymentTransactionRepository);
        this.playerAccountRepository = checkNotNull(playerWalletRepository);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentTransaction process(PaymentTransaction paymentTransaction) {
        // Step 1. Sanity check
        if (paymentTransaction == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.PaymentTransactionEmpty);
        if (!paymentTransaction.valid()) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.PaymentTransactionInvalid);
        }
        // Step 2. Processing payment transactions
        return processTransaction(paymentTransaction);
    }

    private PaymentTransaction processTransaction(PaymentTransaction paymentTransaction) {
        // Step 1. Processing payment transactions
        Collection<PlayerAccount> updatedWallets = new ArrayList<>(paymentTransaction.getPaymentOperations().size());
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
            updatedWallets.add(associatedWallet);
        }
        // Step 2. Performing all account operations at a batch
        playerAccountRepository.save(updatedWallets);
        playerAccountRepository.flush();
        // Step 3. Saving account transaction
        return paymentTransactionRepository.save(paymentTransaction);

    }

    @Override
    public PaymentTransaction getPaymentTransaction(String source, String transactionId) {
        PaymentTransactionKey transactionKey = new PaymentTransactionKey(source, transactionId);
        return paymentTransactionRepository.findOne(transactionKey);
    }

    @Override
    public List<PaymentTransaction> getPaymentTransactions(String player) {
        return paymentTransactionRepository.findByPaymentOperationsPlayer(player);
    }
}
