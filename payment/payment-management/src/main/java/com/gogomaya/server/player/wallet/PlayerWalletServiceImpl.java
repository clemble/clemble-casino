package com.gogomaya.server.player.wallet;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import javax.inject.Inject;

import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.payment.PaymentOperation;
import com.gogomaya.server.repository.player.PlayerWalletRepository;

public class PlayerWalletServiceImpl implements PlayerWalletService {

    final private PlayerWalletRepository playerWalletRepository;

    @Inject
    public PlayerWalletServiceImpl(final PlayerWalletRepository playerWalletRepository) {
        this.playerWalletRepository = checkNotNull(playerWalletRepository);
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
    public boolean canAfford(PaymentOperation walletOperation) {
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

}