package com.clemble.casino.server.payment;

import java.util.Date;
import java.util.Random;

import com.clemble.casino.server.event.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.payment.listener.SystemPaymentTransactionRequestEventListener;
import com.clemble.casino.server.spring.web.payment.PaymentSpringConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.server.repository.payment.PlayerAccountTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PaymentSpringConfiguration.class)
public class PaymentTransactionServiceTest {

    final private Random RANDOM = new Random();

    @Autowired
    public PlayerAccountTemplate accountTemplate;

    @Autowired
    public SystemPaymentTransactionRequestEventListener eventListener;

    private String playerFrom = String.valueOf(RANDOM.nextLong());
    private String playerTo = String.valueOf(RANDOM.nextLong());

    @Before
    public void initialize() {
        accountTemplate.debit(playerFrom, Money.create(Currency.FakeMoney, 100));
        accountTemplate.debit(playerTo, Money.create(Currency.FakeMoney, 50));
    }

    @Test
    public void testWalletUpdate() {
        Money amount = Money.create(Currency.FakeMoney, RANDOM.nextInt(100));

        PaymentTransactionKey transactionKey = new PaymentTransactionKey().setSource("TicTacToe").setTransaction(String.valueOf(RANDOM.nextLong()));

        PaymentTransaction paymentTransaction = new PaymentTransaction()
            .setTransactionKey(transactionKey)
            .setTransactionDate(new Date())
            .addPaymentOperation(new PaymentOperation().setOperation(Operation.Credit).setPlayer(playerFrom).setAmount(amount))
            .addPaymentOperation(new PaymentOperation().setOperation(Operation.Debit).setPlayer(playerTo).setAmount(amount));

        eventListener.onEvent(new SystemPaymentTransactionRequestEvent(paymentTransaction));

        Assert.assertEquals(accountTemplate.findOne(playerTo).getMoney(Currency.FakeMoney).getAmount(), 50 + amount.getAmount());
        Assert.assertEquals(accountTemplate.findOne(playerFrom).getMoney(Currency.FakeMoney).getAmount(), 100 - amount.getAmount());
    }

}