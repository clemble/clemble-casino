package com.gogomaya.server.player.wallet;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.money.Operation;

public class WalletTransactionManagerImpl implements WalletTransactionManager {

    final private PlayerWalletRepository playerWalletRepository;

    final private WalletTransactionRepository walletTransactionRepository;

    @Inject
    public WalletTransactionManagerImpl(final PlayerWalletRepository playerWalletRepository,
            final WalletTransactionRepository walletTransactionRepository) {
        this.playerWalletRepository = checkNotNull(playerWalletRepository);
        this.walletTransactionRepository = checkNotNull(walletTransactionRepository);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void process(WalletTransaction walletTransaction) {
        // Step 1. Sanity check
        if (walletTransaction == null)
            throw GogomayaException.create(GogomayaError.PaymentTransactionEmpty);
        if (!walletTransaction.valid())
            throw GogomayaException.create(GogomayaError.PaymentTransactionInvalid);
        // Step 2. Processing wallet transactions
        for (WalletOperation walletOperation : walletTransaction.getWalletOperations()) {
            PlayerWallet associatedWallet = playerWalletRepository.findOne(walletOperation.getPlayerId());
            if (walletOperation.getOperation() == Operation.Credit) {
                associatedWallet.subtract(walletOperation.getAmmount());
            } else {
                associatedWallet.add(walletOperation.getAmmount());
            }
            playerWalletRepository.save(associatedWallet);
        }
        walletTransactionRepository.save(walletTransaction);
    }

}
