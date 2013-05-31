package com.gogomaya.server.player.wallet;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.Operation;

public class WalletTransactionManagerImpl implements WalletTransactionManager {

    final private PlayerWalletRepository playerWalletRepository;

    final private WalletTransactionRepository walletTransactionRepository;

    @Inject
    public WalletTransactionManagerImpl(final PlayerWalletRepository playerWalletRepository, final WalletTransactionRepository walletTransactionRepository) {
        this.playerWalletRepository = checkNotNull(playerWalletRepository);
        this.walletTransactionRepository = checkNotNull(walletTransactionRepository);
    }

    @Override
    public boolean canAfford(WalletOperation walletOperation) {
        // Step 1. Checking positive operation
        if (walletOperation == null)
            return true;
        if (walletOperation.getOperation() == Operation.Debit && walletOperation.getAmmount().isPositive())
            return true;
        if (walletOperation.getOperation() == Operation.Credit && walletOperation.getAmmount().isNegative())
            return true;
        // Step 2. Checking player wallet value
        PlayerWallet wallet = playerWalletRepository.findOne(walletOperation.getPlayerId());
        Money existingAmmount = wallet.getMoney(walletOperation.getAmmount().getCurrency());
        // Step 2.1 If exising ammount is not enough player can't afford it
        return existingAmmount.getAmount() > walletOperation.getAmmount().getAmount();
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
