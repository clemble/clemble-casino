package com.clemble.casino.integration.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameState;
import com.clemble.casino.integration.game.GameSessionPlayer;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.integration.util.ClembleCasinoExceptionMatcherFactory;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.event.PaymentEvent;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class PlayerAccountOperationsITest {

    @Autowired
    public PlayerScenarios playerOperations;

    @Autowired
    public GameScenarios gameOperations;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testInitialAmount() throws InterruptedException {
        // Step 1. Creating random player A
        ClembleCasinoOperations A = playerOperations.createPlayer();
        // Step 1.1 Registering bonus event listener
        final BlockingQueue<PaymentEvent> bonusEvents = new ArrayBlockingQueue<>(2);
        A.paymentOperations().subscribe(new EventListener<PaymentEvent>() {
            @Override
            public void onEvent(PaymentEvent event) {
                bonusEvents.add(event);
            }
        });
        // Step 2. Fetching account and precondition
        PlayerAccount accountA = A.paymentOperations().getAccount();
        assertTrue(accountA.getMoney().size() > 0);
        assertTrue(accountA.getMoney(Currency.FakeMoney).getAmount() > 0);
        // Step 3. Checking registration transaction
        A.paymentOperations().getPaymentTransactions();
        PaymentTransaction transaction = A.paymentOperations().getPaymentTransaction("registration", A.getPlayer());
        Set<PaymentOperation> paymentOperations = transaction.getPaymentOperations();
        Money transactionAmmount = paymentOperations.iterator().next().getAmount();
        // Step 4. Checking bonus transaction (Which might be delayed, because of the system event delays)
        if(A.paymentOperations().getPaymentTransactions("dailybonus").isEmpty()) {
            bonusEvents.poll(30, TimeUnit.SECONDS);
        }
        PaymentTransaction bonusTransaction = A.paymentOperations().getPaymentTransactions("dailybonus").get(0);
        paymentOperations = bonusTransaction.getPaymentOperations();
        Money bonusAmmount = paymentOperations.iterator().next().getAmount();
        assertEquals(transactionAmmount.add(bonusAmmount.getAmount()), accountA.getMoney(Currency.FakeMoney));
    }

    @Test
    @Ignore
    public void runingOutOfMoney() {
        ClembleCasinoOperations A = playerOperations.createPlayer();
        ClembleCasinoOperations B = playerOperations.createPlayer();

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.GameConstructionInsufficientMoney));

        do {
            GameSessionPlayer<GameState> AvsB = gameOperations.<GameState> construct(Game.num, A, B.getPlayer());
            GameSessionPlayer<GameState> BvsA = gameOperations.<GameState> accept(AvsB.getSession(), B);

            AvsB.waitForStart();
            BvsA.waitForStart();

            AvsB.giveUp();
        } while (true);
    }

}