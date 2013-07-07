package com.gogomaya.server.player.wallet;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.repository.payment.PlayerWalletRepository;
import com.gogomaya.server.repository.payment.WalletTransactionRepository;

public class WalletTransactionManagerImpl implements WalletTransactionManager {

    final private PlayerWalletRepository playerWalletRepository;

    final private WalletTransactionRepository walletTransactionRepository;

    @Inject
    public WalletTransactionManagerImpl(final PlayerWalletRepository playerWalletRepository, final WalletTransactionRepository walletTransactionRepository) {
        this.playerWalletRepository = checkNotNull(playerWalletRepository);
        this.walletTransactionRepository = checkNotNull(walletTransactionRepository);
    }

    @Override
    public boolean canAfford(long playerId, Money ammount) {
        // Step 1. Retrieving players wallet
        PlayerWallet wallet = playerWalletRepository.findOne(playerId);
        Money existingAmmount = wallet.getMoney(ammount.getCurrency());
        // Step 2. If exising ammount is not enough player can't afford it
        return existingAmmount.getAmount() >= ammount.getAmount();
    }

    @Override
    public boolean canAfford(Collection<Long> players, Money ammount) {
        for (Long player : players) {
            if (!canAfford(player, ammount))
                return false;
        }
        return true;
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
        // Step 2. Checking if player can afford this
        return canAfford(walletOperation.getPlayerId(), walletOperation.getAmmount());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void process(WalletTransaction walletTransaction) {
        // Step 1. Sanity check
        if (walletTransaction == null)
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionEmpty);
        if (!walletTransaction.valid())
            throw GogomayaException.fromError(GogomayaError.PaymentTransactionInvalid);
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
