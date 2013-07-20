package com.gogomaya.server.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import javax.inject.Inject;

import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.payment.PaymentOperation;
import com.gogomaya.server.player.account.PlayerAccount;
import com.gogomaya.server.player.account.PlayerAccountService;
import com.gogomaya.server.repository.player.PlayerAccountRepository;

public class PlayerAccountServiceImpl implements PlayerAccountService {

    final private PlayerAccountRepository playerWalletRepository;

    @Inject
    public PlayerAccountServiceImpl(final PlayerAccountRepository playerWalletRepository) {
        this.playerWalletRepository = checkNotNull(playerWalletRepository);
    }

    @Override
    public boolean canAfford(long playerId, Money ammount) {
        // Step 1. Retrieving players account
        PlayerAccount playerAccount = playerWalletRepository.findOne(playerId);
        Money existingAmmount = playerAccount.getMoney(ammount.getCurrency());
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
    public boolean canAfford(PaymentOperation paymentOperation) {
        // Step 1. Checking positive operation
        if (paymentOperation == null)
            return true;
        if (paymentOperation.getOperation() == Operation.Debit && paymentOperation.getAmmount().isPositive())
            return true;
        if (paymentOperation.getOperation() == Operation.Credit && paymentOperation.getAmmount().isNegative())
            return true;
        // Step 2. Checking if player can afford this
        return canAfford(paymentOperation.getPlayerId(), paymentOperation.getAmmount());
    }

}
