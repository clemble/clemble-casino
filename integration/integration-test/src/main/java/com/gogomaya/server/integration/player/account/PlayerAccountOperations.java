package com.gogomaya.server.integration.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.money.MoneySource;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.server.integration.player.Player;

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

    public PlayerAccount getAccount(String playerId) {
        return accountOperations.getAccount(player, playerId);
    }

    public List<PaymentTransaction> getTransactions() {
        return accountOperations.getTransactions(player);
    }

    public List<PaymentTransaction> getTransactions(String playerId) {
        return accountOperations.getTransactions(player, playerId);
    }

    public PaymentTransaction getTransaction(MoneySource moneySource, String transactionId) {
        return accountOperations.getTransaction(player, moneySource, transactionId);
    }

    public PaymentTransaction getTransaction(String playerId, MoneySource moneySource, String transactionId) {
        return accountOperations.getTransaction(player, playerId, moneySource, transactionId);
    }

    public PaymentTransaction getTransaction(String moneySource, String transactionId) {
        return accountOperations.getTransaction(player, moneySource, transactionId);
    }

    public PaymentTransaction getTransaction(String playerId, String moneySource, String transactionId) {
        return accountOperations.getTransaction(player, playerId, moneySource, transactionId);
    }

}
