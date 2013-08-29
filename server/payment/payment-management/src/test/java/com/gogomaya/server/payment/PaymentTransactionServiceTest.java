package com.gogomaya.server.payment;

import java.util.Random;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.money.Currency;
import com.gogomaya.money.Money;
import com.gogomaya.money.MoneySource;
import com.gogomaya.money.Operation;
import com.gogomaya.payment.PaymentOperation;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PaymentTransactionId;
import com.gogomaya.payment.PaymentTransactionService;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.server.repository.player.PlayerAccountRepository;
import com.gogomaya.server.spring.payment.PaymentManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PaymentManagementSpringConfiguration.class)
public class PaymentTransactionServiceTest {

    final private Random RANDOM = new Random();

    @Inject
    public PlayerAccountRepository playerAccountRepository;

    @Inject
    public PaymentTransactionService paymentTransactionService;

    private long playerFrom = RANDOM.nextLong();
    private long playerTo = RANDOM.nextLong();

    @Before
    public void initialize() {
        PlayerAccount accountFrom = new PlayerAccount().add(Money.create(Currency.FakeMoney, 100)).setPlayerId(playerFrom);

        PlayerAccount accountTo = new PlayerAccount().add(Money.create(Currency.FakeMoney, 50)).setPlayerId(playerTo);

        playerAccountRepository.saveAndFlush(accountFrom);
        playerAccountRepository.saveAndFlush(accountTo);
    }

    @Test
    public void testWalletUpdate() {
        Money amount = Money.create(Currency.FakeMoney, RANDOM.nextInt(100));

        PaymentTransactionId transactionId = new PaymentTransactionId().setSource(MoneySource.TicTacToe).setTransactionId(1L);

        PaymentTransaction paymentTransaction = new PaymentTransaction().setTransactionId(transactionId)
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Credit).setPlayerId(playerFrom).setAmount(amount))
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Debit).setPlayerId(playerTo).setAmount(amount));

        paymentTransactionService.process(paymentTransaction);

        Assert.assertEquals(playerAccountRepository.findOne(playerTo).getMoney(Currency.FakeMoney).getAmount(), 50 + amount.getAmount());
        Assert.assertEquals(playerAccountRepository.findOne(playerFrom).getMoney(Currency.FakeMoney).getAmount(), 100 - amount.getAmount());
    }

}
