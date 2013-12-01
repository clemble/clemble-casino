package com.clemble.casino.client.payment;

import java.util.List;

import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.player.PlayerAware;

public interface PaymentOperations extends PlayerAware {

    public PlayerAccount getAccount();

    public List<PaymentTransaction> getPaymentTransactions();

    public PaymentTransaction getPaymentTransaction(MoneySource source, String transactionId);

    public PaymentTransaction getPaymentTransaction(String source, String transactionId);

}
