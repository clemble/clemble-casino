package com.clemble.casino.server.payment;

import java.util.Date;
import java.util.Random;

import com.clemble.casino.server.event.payment.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.event.player.SystemPlayerCreatedEvent;
import com.clemble.casino.server.payment.listener.SystemPaymentTransactionRequestEventListener;
import com.clemble.casino.server.payment.listener.SystemPlayerAccountCreationEventListener;
import com.clemble.casino.server.payment.spring.PaymentSpringConfiguration;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.server.payment.repository.PlayerAccountTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PaymentSpringConfiguration.class)
public class PaymentTransactionServiceTest {

    final private Random RANDOM = new Random();

    @Autowired
    public SystemPlayerAccountCreationEventListener accountCreator;

    @Autowired
    public PlayerAccountTemplate accountTemplate;

    @Autowired
    public SystemPaymentTransactionRequestEventListener eventListener;

    private String playerFrom = String.valueOf(RANDOM.nextLong());
    private String playerTo = String.valueOf(RANDOM.nextLong());

    @Before
    public void initialize() {
        accountCreator.onEvent(new SystemPlayerCreatedEvent(playerFrom));
        accountCreator.onEvent(new SystemPlayerCreatedEvent(playerTo));

        accountTemplate.process(playerFrom + ":registration", new PaymentOperation(playerFrom, Money.create(Currency.FakeMoney, 100), Operation.Debit));
        accountTemplate.process(playerTo + ":registration", new PaymentOperation(playerTo, Money.create(Currency.FakeMoney, 100), Operation.Debit));
    }

    @Test
    public void testWalletUpdate() {
        Money amount = Money.create(Currency.FakeMoney, RANDOM.nextInt(100));

        String transactionKey = "TicTacToe" + RANDOM.nextLong();

        PaymentTransaction paymentTransaction = new PaymentTransaction()
            .setTransactionKey(transactionKey)
            .setTransactionDate(DateTime.now(DateTimeZone.UTC))
            .addOperation(new PaymentOperation(playerFrom, amount, Operation.Credit))
            .addOperation(new PaymentOperation(playerTo, amount, Operation.Debit));

        eventListener.onEvent(new SystemPaymentTransactionRequestEvent(paymentTransaction));

        Assert.assertEquals(accountTemplate.findOne(playerTo).getMoney(Currency.FakeMoney).getAmount(), 100 + amount.getAmount());
        Assert.assertEquals(accountTemplate.findOne(playerFrom).getMoney(Currency.FakeMoney).getAmount(), 100 - amount.getAmount());
    }

}
