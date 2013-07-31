package com.gogomaya.server.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import javax.inject.Inject;

import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.payment.PaymentOperation;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.payment.PaymentTransactionId;
import com.gogomaya.server.payment.PaymentTransactionService;
import com.gogomaya.server.player.PlayerAware;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.repository.player.PlayerAccountRepository;

public class PlayerAccountServiceImpl implements PlayerAccountService {

    final private PlayerAccountRepository playerAccountRepository;
    final private PaymentTransactionService paymentTransactionService;

    @Inject
    public PlayerAccountServiceImpl(final PlayerAccountRepository playerWalletRepository, final PaymentTransactionService paymentTransactionService) {
        this.playerAccountRepository = checkNotNull(playerWalletRepository);
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
    }

    @Override
    public PlayerAccount register(PlayerProfile player) {
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
        paymentTransactionService.process(initialTransaction);
        return playerAccountRepository.findOne(player.getPlayerId());
    }

    @Override
    public boolean canAfford(long playerId, Money ammount) {
        // Step 1. Retrieving players account
        PlayerAccount playerAccount = playerAccountRepository.findOne(playerId);
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

}
