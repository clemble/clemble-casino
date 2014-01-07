package com.clemble.casino.server.payment;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.bonus.PaymentBonusSource;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.SystemPlayerEnteredEvent;
import com.clemble.casino.server.payment.bonus.DailyBonusService;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.server.repository.payment.PlayerAccountRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.payment.PaymentManagementSpringConfiguration;
import com.clemble.test.random.ObjectGenerator;

@ActiveProfiles(SpringConfiguration.UNIT_TEST)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PaymentManagementSpringConfiguration.class)
public class DailyBonusServiceTest {

    @Autowired
    public DailyBonusService dailyBonusService;

    @Autowired
    public PlayerAccountRepository accountRepository;

    @Autowired
    public PaymentTransactionRepository transactionRepository;

    @Test
    public void checkDailyBonusApplied() {
        // Step 1. Generating player identity
        String player = ObjectGenerator.generate(String.class);
        accountRepository.saveAndFlush(new PlayerAccount(player));
        // Step 2. Creating money
        Money amount = Money.create(Currency.FakeMoney, 100);
        PaymentTransaction paymentTransaction = new PaymentTransaction()
            .setTransactionKey(new PaymentTransactionKey(MoneySource.dailybonus, player))
            .setTransactionDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)))
            .addPaymentOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, amount, Operation.Credit))
            .addPaymentOperation(new PaymentOperation(player, amount, Operation.Debit));
        transactionRepository.saveAndFlush(paymentTransaction);
        assertEquals(transactionRepository.findByPaymentOperationsPlayerAndTransactionKeySourceLike(player, PaymentBonusSource.dailybonus + "%").size(), 1);
        // Step 3. Checking value in payment transaction
        dailyBonusService.onEvent(new SystemPlayerEnteredEvent(player));
        // Step 4. Checking new transaction performed
        assertEquals(transactionRepository.findByPaymentOperationsPlayer(player).size(), 2);
        assertEquals(transactionRepository.findByPaymentOperationsPlayerAndTransactionKeySourceLike(player, PaymentBonusSource.dailybonus + "%").size(), 2);
    }

}
