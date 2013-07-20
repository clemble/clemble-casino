package com.gogomaya.server.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.player.PlayerAware;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.account.PlayerAccount;
import com.gogomaya.server.repository.payment.PaymentTransactionRepository;
import com.gogomaya.server.repository.player.PlayerAccountRepository;

public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    final private PaymentTransactionRepository paymentTransactionRepository;
    final private PlayerAccountRepository playerAccountRepository;

    public PaymentTransactionServiceImpl(PaymentTransactionRepository paymentTransactionRepository, PlayerAccountRepository playerWalletRepository) {
        this.paymentTransactionRepository = checkNotNull(paymentTransactionRepository);
        this.playerAccountRepository = checkNotNull(playerWalletRepository);
    }

    @Override
    public PaymentTransaction register(PlayerProfile player) {
        // Step 1. Creating initial empty account
        PlayerAccount initialWallet = new PlayerAccount().setPlayerId(player.getPlayerId());
        initialWallet = playerAccountRepository.save(initialWallet);
        // Step 2. Creating initial empty
        Money initialBalance = Money.create(Currency.FakeMoney, 500);
        PaymentTransaction initialTransaction = new PaymentTransaction()
                .setTransactionId(new PaymentTransactionId(MoneySource.Registration, player.getPlayerId()))
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Debit).setAmmount(initialBalance).setPlayerId(player.getPlayerId()))
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Credit).setAmmount(initialBalance).setPlayerId(PlayerAware.DEFAULT_PLAYER));
        // Step 3. Returning PaymentTransaction
        return processTransaction(initialTransaction);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentTransaction process(PaymentTransaction paymentTransaction) {
        // Step 1. Sanity check
        if (paymentTransaction == null)
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionEmpty);
        if (!paymentTransaction.valid())
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionInvalid);
        // Step 2. Processing payment transactions
        return processTransaction(paymentTransaction);
    }

    private PaymentTransaction processTransaction(PaymentTransaction paymentTransaction) {
        // Step 1. Processing payment transactions
        Collection<PlayerAccount> updatedWallets = new ArrayList<>(paymentTransaction.getPaymentOperations().size());
        for (PaymentOperation paymentOperation : paymentTransaction.getPaymentOperations()) {
            PlayerAccount associatedWallet = playerAccountRepository.findOne(paymentOperation.getPlayerId());
            if (associatedWallet == null) {
                throw GogomayaException.fromError(GogomayaError.PaymentTransactionUnknownPlayers);
            } else if (paymentOperation.getOperation() == Operation.Credit) {
                associatedWallet.subtract(paymentOperation.getAmmount());
            } else if (paymentOperation.getOperation() == Operation.Debit) {
                associatedWallet.add(paymentOperation.getAmmount());
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
