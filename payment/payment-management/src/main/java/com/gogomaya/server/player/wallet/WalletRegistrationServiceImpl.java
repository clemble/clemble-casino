package com.gogomaya.server.player.wallet;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;

public class WalletRegistrationServiceImpl implements WalletRegistrationService {

    final private PlayerWalletRepository walletRepository;

    public WalletRegistrationServiceImpl(PlayerWalletRepository walletRepository) {
        this.walletRepository = checkNotNull(walletRepository);
    }

    @Override
    public void register(long player) {
        // Step 1. Creating initial balance
        Money initialBalance = Money.create(Currency.FakeMoney, 500);
        // Step 2. Creating initial wallet
        PlayerWallet initialWallet = new PlayerWallet()
            .setPlayerId(player)
            .add(initialBalance);
        // Step 3. Creating wallet repository
        walletRepository.save(initialWallet);
    }

}
