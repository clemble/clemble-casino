package com.clemble.casino.integration.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

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
    public void testInitialAmount() {
        // Step 1. Creating random player A
        ClembleCasinoOperations A = playerOperations.createPlayer();
        // Step 2. Fetching account and precondition
        PlayerAccount accountA = A.paymentOperations().getAccount();
        assertTrue(accountA.getMoney().size() > 0);
        assertTrue(accountA.getMoney(Currency.FakeMoney).getAmount() > 0);
        // Step 3. Processing next transactions
        A.paymentOperations().getPaymentTransactions();
        PaymentTransaction transaction = A.paymentOperations().getPaymentTransaction("registration", A.getPlayer());
        Set<PaymentOperation> paymentOperations = transaction.getPaymentOperations();
        Money transactionAmmount = paymentOperations.iterator().next().getAmount();

        assertEquals(transactionAmmount, accountA.getMoney(Currency.FakeMoney));
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
