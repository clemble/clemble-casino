package com.gogomaya.server.payment;

import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.money.Currency;
import com.gogomaya.money.Money;
import com.gogomaya.money.MoneySource;
import com.gogomaya.money.Operation;
import com.gogomaya.payment.PaymentOperation;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PaymentTransactionKey;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.server.repository.player.PlayerAccountRepository;
import com.gogomaya.server.spring.payment.PaymentManagementSpringConfiguration;

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
