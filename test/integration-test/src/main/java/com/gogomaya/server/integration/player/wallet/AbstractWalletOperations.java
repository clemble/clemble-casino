package com.gogomaya.server.integration.player.wallet;

import java.util.List;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.player.wallet.PlayerWallet;

abstract public class AbstractWalletOperations implements WalletOperations {

    @Override
    final public PlayerWallet getWallet(Player player) {
        return getWallet(player, player.getPlayerId());
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
