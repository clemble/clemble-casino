package com.gogomaya.server.integration.player.account;

import java.util.List;

import com.gogomaya.money.MoneySource;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.server.integration.player.Player;

public interface AccountOperations {

    public PlayerAccount getAccount(Player player);

    public PlayerAccount getAccount(Player player, String playerKey);

    public List<PaymentTransaction> getTransactions(Player player);

    public List<PaymentTransaction> getTransactions(Player player, String playerKey);

    public PaymentTransaction getTransaction(Player player, MoneySource moneySource, String transactionId);

    public PaymentTransaction getTransaction(Player player, String playerKey, MoneySource moneySource, String transactionId);

    public PaymentTransaction getTransaction(Player player, String moneySource, String transactionId);

    public PaymentTransaction getTransaction(Player player, String playerKey, String moneySource, String transactionId);

}
