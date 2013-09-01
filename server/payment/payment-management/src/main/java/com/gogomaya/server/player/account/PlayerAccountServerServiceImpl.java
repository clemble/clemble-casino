package com.gogomaya.server.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.money.Currency;
import com.gogomaya.money.Money;
import com.gogomaya.money.MoneySource;
import com.gogomaya.money.Operation;
import com.gogomaya.payment.PaymentOperation;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PaymentTransactionId;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.player.PlayerAware;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.server.payment.PaymentTransactionServerService;
import com.gogomaya.server.repository.player.PlayerAccountRepository;

public class PlayerAccountServerServiceImpl implements PlayerAccountServerService {

    final private PlayerAccountRepository playerAccountRepository;
    final private PaymentTransactionServerService paymentTransactionService;

    public PlayerAccountServerServiceImpl(final PlayerAccountRepository playerWalletRepository, final PaymentTransactionServerService paymentTransactionService) {
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
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Debit).setAmount(initialBalance).setPlayerId(player.getPlayerId()))
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Credit).setAmount(initialBalance).setPlayerId(PlayerAware.DEFAULT_PLAYER));
        // Step 3. Returning PaymentTransaction
        paymentTransactionService.process(initialTransaction);
        return playerAccountRepository.findOne(player.getPlayerId());
    }

    @Override
    public boolean canAfford(long playerId, Money amount) {
        // Step 1. Retrieving players account
        PlayerAccount playerAccount = playerAccountRepository.findOne(playerId);
        Money existingAmmount = playerAccount.getMoney(amount.getCurrency());
        // Step 2. If existing amount is not enough player can't afford it
        return existingAmmount.getAmount() >= amount.getAmount();
    }

    @Override
    public boolean canAfford(Collection<Long> players, Money amount) {
        for (Long player : players) {
            if (!canAfford(player, amount))
                return false;
        }
        return true;
    }

}
