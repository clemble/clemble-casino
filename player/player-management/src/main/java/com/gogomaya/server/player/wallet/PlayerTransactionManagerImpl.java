package com.gogomaya.server.player.wallet;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.server.money.Money;

@Service("playerWalletManager")
public class PlayerTransactionManagerImpl implements PlayerMoneyTransactionManager {

    final private PlayerWalletRepository playerWalletRepository;

    @Inject
    public PlayerTransactionManagerImpl(PlayerWalletRepository playerWalletRepository) {
        this.playerWalletRepository = checkNotNull(playerWalletRepository);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void debit(long playerFrom, long playerTo, Money ammount) {
        // Step 1. Looking and locking accounts
        PlayerWallet walletFrom = playerWalletRepository.findOne(playerFrom);
        PlayerWallet walletTo = playerWalletRepository.findOne(playerTo);
        // Step 2. Performing necessary changes
        walletFrom.add(ammount.negate());
        walletTo.add(ammount);
        // Step 3. Saving updated data
        playerWalletRepository.saveAndFlush(walletTo);
        playerWalletRepository.saveAndFlush(walletFrom);
    }

}
