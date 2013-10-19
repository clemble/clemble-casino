package com.clemble.casino.server.payment;

import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.server.payment.PaymentTransactionServerService;
import com.clemble.casino.server.repository.payment.PlayerAccountRepository;
import com.clemble.casino.server.spring.payment.PaymentManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PaymentManagementSpringConfiguration.class)
public class PaymentTransactionServiceTest {

    final private Random RANDOM = new Random();

    @Autowired
    public PlayerAccountRepository playerAccountRepository;

    @Autowired
    public PaymentTransactionServerService paymentTransactionService;

    private String playerFrom = String.valueOf(RANDOM.nextLong());
    private String playerTo = String.valueOf(RANDOM.nextLong());

    @Before
    public void initialize() {
        PlayerAccount accountFrom = new PlayerAccount().add(Money.create(Currency.FakeMoney, 100)).setPlayer(playerFrom);

        PlayerAccount accountTo = new PlayerAccount().add(Money.create(Currency.FakeMoney, 50)).setPlayer(playerTo);

        playerAccountRepository.saveAndFlush(accountFrom);
        playerAccountRepository.saveAndFlush(accountTo);
    }

    @Test
    public void testWalletUpdate() {
        Money amount = Money.create(Currency.FakeMoney, RANDOM.nextInt(100));

        PaymentTransactionKey transactionId = new PaymentTransactionKey().setSource(MoneySource.TicTacToe).setTransaction("1");

        PaymentTransaction paymentTransaction = new PaymentTransaction().setTransactionKey(transactionId)
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Credit).setPlayer(playerFrom).setAmount(amount))
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Debit).setPlayer(playerTo).setAmount(amount));

        paymentTransactionService.process(paymentTransaction);

        Assert.assertEquals(playerAccountRepository.findOne(playerTo).getMoney(Currency.FakeMoney).getAmount(), 50 + amount.getAmount());
        Assert.assertEquals(playerAccountRepository.findOne(playerFrom).getMoney(Currency.FakeMoney).getAmount(), 100 - amount.getAmount());
    }

}
