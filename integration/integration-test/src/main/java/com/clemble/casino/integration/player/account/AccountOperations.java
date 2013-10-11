package com.clemble.casino.integration.player.account;

import java.util.List;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;

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
