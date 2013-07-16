package com.gogomaya.server.integration.player.wallet;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.player.wallet.PlayerWallet;

public class PlayerWalletOperations {

    final private Player player;
    final WalletOperations walletOperations;

    public PlayerWalletOperations(Player player, WalletOperations walletOperations) {
        this.player = checkNotNull(player);
        this.walletOperations = checkNotNull(walletOperations);
    }

    public PlayerWallet getWallet() {
        return walletOperations.getWallet(player);
    }

    public PlayerWallet getWallet(long playerId) {
        return walletOperations.getWallet(player, playerId);
    }

    public List<PaymentTransaction> getTransactions() {
        return walletOperations.getTransactions(player);
    }

    public List<PaymentTransaction> getTransactions(long playerId) {
        return walletOperations.getTransactions(player, playerId);
    }

    public PaymentTransaction getTransaction(MoneySource moneySource, long transactionId) {
        return walletOperations.getTransaction(player, moneySource, transactionId);
    }

    public PaymentTransaction getTransaction(long playerId, MoneySource moneySource, long transactionId) {
        return walletOperations.getTransaction(player, playerId, moneySource, transactionId);
    }

    public PaymentTransaction getTransaction(String moneySource, long transactionId) {
        return walletOperations.getTransaction(player, moneySource, transactionId);
    }

    public PaymentTransaction getTransaction(long playerId, String moneySource, long transactionId) {
        return walletOperations.getTransaction(player, playerId, moneySource, transactionId);
    }

}
