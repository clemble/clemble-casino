package com.clemble.casino.integration.bonus;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.test.concurrent.AsyncCompletionUtils;
import com.clemble.test.concurrent.Check;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.bonus.PaymentBonusSource;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.player.SystemPlayerEnteredEvent;
import com.clemble.casino.server.bonus.listener.DailyBonusEventListener;
import com.clemble.casino.server.payment.repository.PaymentTransactionRepository;
import com.clemble.casino.server.payment.repository.PlayerAccountTemplate;
import com.clemble.test.random.ObjectGenerator;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = IntegrationTestSpringConfiguration.class)
public class DailyBonusServiceTest {

    @Autowired
    public DailyBonusEventListener dailyBonusService;

    @Autowired
    public PlayerAccountTemplate accountRepository;

    @Autowired
    public PaymentTransactionRepository transactionRepository;

    @Test
    public void checkDailyBonusApplied() {
        // Step 1. Generating player identity
        final String player = ObjectGenerator.generate(String.class);
        // Step 2. Creating money
        Money amount = Money.create(Currency.FakeMoney, 100);
        PaymentTransaction paymentTransaction = new PaymentTransaction()
            .setTransactionKey(PaymentBonusSource.dailybonus + player)
            .setTransactionDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)))
            .addPaymentOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, amount, Operation.Credit))
            .addPaymentOperation(new PaymentOperation(player, amount, Operation.Debit));
        transactionRepository.save(paymentTransaction);
        assertEquals(transactionRepository.findByPaymentOperationsPlayerAndTransactionKeyLike(player, PaymentBonusSource.dailybonus.name()).size(), 1);
        // Step 3. Checking value in payment transaction
        dailyBonusService.onEvent(new SystemPlayerEnteredEvent(player));
        // Step 4. Checking new transaction performed
        AsyncCompletionUtils.check(new Check() {
            @Override
            public boolean check() {
                return transactionRepository.findByPaymentOperationsPlayer(player).size() ==  2;
            }
        }, 5000);
        assertEquals(transactionRepository.findByPaymentOperationsPlayer(player).size(), 2);
        assertEquals(transactionRepository.findByPaymentOperationsPlayerAndTransactionKeyLike(player, PaymentBonusSource.dailybonus.name()).size(), 2);
    }

}
