package com.gogomaya.server.integration.player.account;

import java.util.List;

import com.gogomaya.money.MoneySource;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.server.integration.player.Player;

abstract public class AbstractAccountOperations implements AccountOperations {

    @Override
    final public PlayerAccount getAccount(Player player) {
        return getAccount(player, player.getPlayerId());
    }

    @Override
    final public List<PaymentTransaction> getTransactions(Player player) {
        return getTransactions(player, player.getPlayerId());
    }

    @Override
    final public PaymentTransaction getTransaction(Player player, String moneySource, long transactionId) {
        return getTransaction(player, player.getPlayerId(), moneySource, transactionId);
    }

    @Override
    final public PaymentTransaction getTransaction(Player player, MoneySource moneySource, long transactionId) {
        return getTransaction(player, moneySource.name(), transactionId);
    }

    @Override
    final public PaymentTransaction getTransaction(Player player, long playerId, MoneySource moneySource, long transactionId) {
        return getTransaction(player, playerId, moneySource.name(), transactionId);
    }

}
