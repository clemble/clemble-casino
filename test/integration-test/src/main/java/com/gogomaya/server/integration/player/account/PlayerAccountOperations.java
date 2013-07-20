package com.gogomaya.server.integration.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.player.account.PlayerAccount;

public class PlayerAccountOperations {

    final private Player player;
    final AccountOperations accountOperations;

    public PlayerAccountOperations(Player player, AccountOperations accountOperations) {
        this.player = checkNotNull(player);
        this.accountOperations = checkNotNull(accountOperations);
    }

    public PlayerAccount getAccount() {
        return accountOperations.getAccount(player);
    }

    public PlayerAccount getAccount(long playerId) {
        return accountOperations.getAccount(player, playerId);
    }

    public List<PaymentTransaction> getTransactions() {
        return accountOperations.getTransactions(player);
    }

    public List<PaymentTransaction> getTransactions(long playerId) {
        return accountOperations.getTransactions(player, playerId);
    }

    public PaymentTransaction getTransaction(MoneySource moneySource, long transactionId) {
        return accountOperations.getTransaction(player, moneySource, transactionId);
    }

    public PaymentTransaction getTransaction(long playerId, MoneySource moneySource, long transactionId) {
        return accountOperations.getTransaction(player, playerId, moneySource, transactionId);
    }

    public PaymentTransaction getTransaction(String moneySource, long transactionId) {
        return accountOperations.getTransaction(player, moneySource, transactionId);
    }

    public PaymentTransaction getTransaction(long playerId, String moneySource, long transactionId) {
        return accountOperations.getTransaction(player, playerId, moneySource, transactionId);
    }

}
