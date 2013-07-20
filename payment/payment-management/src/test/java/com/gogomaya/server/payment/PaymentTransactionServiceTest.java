package com.gogomaya.server.payment;

import javax.inject.Inject;

import org.junit.Assert;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.MoneySource;
import com.gogomaya.server.money.Operation;
import com.gogomaya.server.payment.PaymentOperation;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.payment.PaymentTransactionId;
import com.gogomaya.server.payment.PaymentTransactionService;
import com.gogomaya.server.player.account.PlayerAccount;
import com.gogomaya.server.repository.player.PlayerAccountRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.payment.PaymentManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(SpringConfiguration.PROFILE_TEST)
@ContextConfiguration(classes = PaymentManagementSpringConfiguration.class)
public class PaymentTransactionServiceTest {

    @Inject
    public PlayerAccountRepository playerAccountRepository;

    @Inject
    public PaymentTransactionService paymentTransactionService;

    private long playerFrom = RandomUtils.nextLong();
    private long playerTo = RandomUtils.nextLong();

    @Before
    public void initialize() {
        PlayerAccount accountFrom = new PlayerAccount().add(Money.create(Currency.FakeMoney, 100)).setPlayerId(playerFrom);

        PlayerAccount accountTo = new PlayerAccount().add(Money.create(Currency.FakeMoney, 50)).setPlayerId(playerTo);

        playerAccountRepository.saveAndFlush(accountFrom);
        playerAccountRepository.saveAndFlush(accountTo);
    }

    @Test
    public void testWalletUpdate() {
        Money ammount = Money.create(Currency.FakeMoney, RandomUtils.nextInt(100));

        PaymentTransactionId transactionId = new PaymentTransactionId().setSource(MoneySource.TicTacToe).setTransactionId(1L);

        PaymentTransaction paymentTransaction = new PaymentTransaction().setTransactionId(transactionId)
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Credit).setPlayerId(playerFrom).setAmmount(ammount))
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Debit).setPlayerId(playerTo).setAmmount(ammount));

        paymentTransactionService.process(paymentTransaction);

        Assert.assertEquals(playerAccountRepository.findOne(playerTo).getMoney(Currency.FakeMoney).getAmount(), 50 + ammount.getAmount());
        Assert.assertEquals(playerAccountRepository.findOne(playerFrom).getMoney(Currency.FakeMoney).getAmount(), 100 - ammount.getAmount());
    }

}
