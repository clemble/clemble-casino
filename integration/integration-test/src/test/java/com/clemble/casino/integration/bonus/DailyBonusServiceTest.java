package com.clemble.casino.integration.bonus;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.test.concurrent.AsyncCompletionUtils;
import com.clemble.test.concurrent.Check;
import org.joda.time.DateTime;
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
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.player.SystemPlayerEnteredEvent;
import com.clemble.casino.server.bonus.listener.DailyBonusEventListener;
import com.clemble.casino.server.payment.repository.PaymentTransactionRepository;
import com.clemble.casino.server.payment.repository.ServerAccountService;
import com.clemble.test.random.ObjectGenerator;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class DailyBonusServiceTest {

    @Autowired
    public DailyBonusEventListener dailyBonusService;

    @Autowired
    public ServerAccountService accountRepository;

    @Autowired
    public PaymentTransactionRepository transactionRepository;

    @Test
    public void checkDailyBonusApplied() {
        // Step 1. Generating player identity
        final String player = ObjectGenerator.generate(String.class);
        // Step 2. Creating money
        Money amount = Money.create(Currency.FakeMoney, 100);
        PaymentTransaction paymentTransaction = new PaymentTransaction()
            .setTransactionKey("dailybonus" + player)
            .setTransactionDate(new DateTime(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)))
            .addOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, amount, Operation.Credit))
            .addOperation(new PaymentOperation(player, amount, Operation.Debit));
        transactionRepository.save(paymentTransaction);
        assertEquals(transactionRepository.findByOperationsPlayerAndTransactionKeyLike(player, "dailybonus").size(), 1);
        // Step 3. Checking value in payment transaction
        dailyBonusService.onEvent(new SystemPlayerEnteredEvent(player));
        // Step 4. Checking new transaction performed
        AsyncCompletionUtils.check(new Check() {
            @Override
            public boolean check() {
                return transactionRepository.findByOperationsPlayer(player).size() ==  2;
            }
        }, 5000);
        assertEquals(transactionRepository.findByOperationsPlayer(player).size(), 2);
        assertEquals(transactionRepository.findByOperationsPlayerAndTransactionKeyLike(player, "dailybonus").size(), 2);
    }

}
