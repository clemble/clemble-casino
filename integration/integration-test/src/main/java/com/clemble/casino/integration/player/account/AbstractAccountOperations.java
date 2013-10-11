package com.clemble.casino.integration.player.account;

import java.util.List;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;

abstract public class AbstractAccountOperations implements AccountOperations {

    @Override
    final public PlayerAccount getAccount(Player player) {
        return getAccount(player, player.getPlayer());
    }

    @Override
    final public List<PaymentTransaction> getTransactions(Player player) {
        return getTransactions(player, player.getPlayer());
    }

    @Override
    final public PaymentTransaction getTransaction(Player player, String moneySource, String transactionId) {
        return getTransaction(player, player.getPlayer(), moneySource, transactionId);
    }

    @Override
    final public PaymentTransaction getTransaction(Player player, MoneySource moneySource, String transactionId) {
        return getTransaction(player, moneySource.name(), transactionId);
    }

    @Override
    final public PaymentTransaction getTransaction(Player player, String playerId, MoneySource moneySource, String transactionId) {
        return getTransaction(player, playerId, moneySource.name(), transactionId);
    }

}
