package com.gogomaya.server.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.player.wallet.PlayerWallet;
import com.gogomaya.server.repository.payment.PaymentTransactionRepository;
import com.gogomaya.server.repository.player.PlayerWalletRepository;

public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    final private PaymentTransactionRepository paymentTransactionRepository;
    final private PlayerWalletRepository playerWalletRepository;

    public PaymentTransactionServiceImpl(PaymentTransactionRepository paymentTransactionRepository, PlayerWalletRepository playerWalletRepository) {
        this.paymentTransactionRepository = checkNotNull(paymentTransactionRepository);
        this.playerWalletRepository = checkNotNull(playerWalletRepository);
    }

    @Override
    public PlayerWallet register(long player) {
        // Step 1. Creating initial balance
        Money initialBalance = Money.create(Currency.FakeMoney, 500);
        // Step 2. Creating initial wallet
        PlayerWallet initialWallet = new PlayerWallet().setPlayerId(player).add(initialBalance);
        // Step 3. Creating wallet repository
        return playerWalletRepository.save(initialWallet);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentTransaction process(PaymentTransaction walletTransaction) {
        // Step 1. Sanity check
        if (walletTransaction == null)
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionEmpty);
        if (!walletTransaction.valid())
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionInvalid);
        // Step 2. Processing wallet transactions
        for (PaymentOperation walletOperation : walletTransaction.getWalletOperations()) {
            PlayerWallet associatedWallet = playerWalletRepository.findOne(walletOperation.getPlayerId());
            if (walletOperation.getOperation() == Operation.Credit) {
                associatedWallet.subtract(walletOperation.getAmmount());
            } else {
                associatedWallet.add(walletOperation.getAmmount());
            }
            playerWalletRepository.save(associatedWallet);
        }
        return paymentTransactionRepository.save(walletTransaction);
    }
}
