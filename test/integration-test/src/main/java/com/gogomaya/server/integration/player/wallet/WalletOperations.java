package com.gogomaya.server.integration.player.wallet;

import java.util.List;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.player.wallet.PlayerWallet;

public interface WalletOperations {

    public PlayerWallet getWallet(Player player);

    public PlayerWallet getWallet(Player player, long playerId);

    public List<PaymentTransaction> getTransactions(Player player);

    public List<PaymentTransaction> getTransactions(Player player, long playerId);

    public PaymentTransaction getTransaction(Player player, long transactionId);

    public PaymentTransaction getTransaction(Player player, long playerId, long transactionId);

}
