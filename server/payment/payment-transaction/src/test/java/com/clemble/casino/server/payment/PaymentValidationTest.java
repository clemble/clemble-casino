package com.clemble.casino.server.payment;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PendingTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.server.event.payment.SystemPaymentFreezeRequestEvent;
import com.clemble.casino.server.payment.listener.SystemPaymentFreezeRequestEventListener;
import com.clemble.casino.server.payment.repository.PlayerAccountRepository;
import com.clemble.casino.server.payment.spring.PaymentSpringConfiguration;
import com.clemble.casino.test.util.ClembleCasinoExceptionMatcherFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.Map;

/**
 * Created by mavarazy on 16/10/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PaymentSpringConfiguration.class)
public class PaymentValidationTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    public PlayerAccountRepository accountRepository;

    @Autowired
    public SystemPaymentFreezeRequestEventListener freezeRequestEventListener;

    @Test
    public void test(){
        // Step 1. Preparing environment
        String transactionKey = RandomStringUtils.randomAlphabetic(5);
        String player = RandomStringUtils.randomAlphabetic(5);
        Map<Currency, Money> money = Collections.singletonMap(Currency.FakeMoney, Money.create(Currency.FakeMoney, 500));
        PlayerAccount account = new PlayerAccount(player, money, null);
        PlayerAccount saved = accountRepository.save(account);
        // Step 2. Creating appropriate event
        SystemPaymentFreezeRequestEvent event = new SystemPaymentFreezeRequestEvent(new PendingTransaction(transactionKey, Collections.singleton(new PaymentOperation(player, Money.create(Currency.FakeMoney, 50), Operation.Credit)), null));
        // Step 3. Generating exception conditions
        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PaymentTransactionDebitAndCreditNotMatched));
        freezeRequestEventListener.onEvent(event);
    }

}
