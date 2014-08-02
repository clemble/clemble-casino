package com.clemble.casino.server.payment;

import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.server.payment.bonus.PaymentBonusSource;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.server.spring.web.payment.PaymentSpringConfiguration;
import com.clemble.test.random.ObjectGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 7/6/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PaymentSpringConfiguration.class})
public class PaymentTransactionRepositoryTest {

    @Autowired
    public PaymentTransactionRepository transactionRepository;

    @Test
    public void testSave() {
        String player = ObjectGenerator.generate(String.class);
        Money amount = ObjectGenerator.generate(Money.class);
        // Step 1. Creating stub transaction
        PaymentTransaction paymentTransaction = new PaymentTransaction()
            .setTransactionKey(new PaymentTransactionKey(PaymentBonusSource.dailybonus, player))
            .setTransactionDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)))
            .addPaymentOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, amount, Operation.Credit))
            .addPaymentOperation(new PaymentOperation(player, amount, Operation.Debit));
        // Step 2. Saving transaction in repository
        transactionRepository.save(paymentTransaction);
        // Step 3. Retrieving saved transaction
         List<PaymentTransaction> savedTransactions = transactionRepository.findByPaymentOperationsPlayerAndTransactionKeySource(player, PaymentBonusSource.dailybonus.name());
         Assert.assertEquals(savedTransactions.size(), 1);
         Assert.assertEquals(savedTransactions.get(0), paymentTransaction);
    }
}
