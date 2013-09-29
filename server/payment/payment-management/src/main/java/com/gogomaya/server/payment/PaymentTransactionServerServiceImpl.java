package com.gogomaya.server.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.money.Operation;
import com.gogomaya.payment.PaymentOperation;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.server.repository.payment.PaymentTransactionRepository;
import com.gogomaya.server.repository.player.PlayerAccountRepository;

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
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionEmpty);
        if (!paymentTransaction.valid()) {
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionInvalid);
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
                throw GogomayaException.fromError(GogomayaError.PaymentTransactionUnknownPlayers);
            } else if (paymentOperation.getOperation() == Operation.Credit) {
                associatedWallet.subtract(paymentOperation.getAmount());
            } else if (paymentOperation.getOperation() == Operation.Debit) {
                associatedWallet.add(paymentOperation.getAmount());
            } else {
                throw GogomayaException.fromError(GogomayaError.PaymentTransactionInvalid);
            }
            updatedWallets.add(associatedWallet);
        }
        // Step 2. Performing all account operations at a batch
        playerAccountRepository.save(updatedWallets);
        playerAccountRepository.flush();
        // Step 3. Saving account transaction
        return paymentTransactionRepository.save(paymentTransaction);

    }
}
