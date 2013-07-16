package com.gogomaya.server.payment;

import com.gogomaya.server.player.wallet.PlayerWallet;

public interface PaymentTransactionService {

    public PlayerWallet register(long playerId);

    public PaymentTransaction process(PaymentTransaction walletTransaction);

}
