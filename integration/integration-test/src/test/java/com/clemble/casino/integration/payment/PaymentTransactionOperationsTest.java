package com.clemble.casino.integration.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.integration.util.ClembleCasinoExceptionMatcherFactory;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.test.concurrent.Check;
import com.clemble.test.concurrent.CheckDelayUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class PaymentTransactionOperationsTest {

    @Autowired
    public PaymentTransactionOperations paymentTransactionOperations;

    @Autowired
    public PlayerScenarios playerOperations;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    @Ignore // TODO find a solution, if it's not too costly
    public void testFakePaymentTransaction() {
        PaymentTransaction paymentTransaction = new PaymentTransaction().setTransactionKey(new PaymentTransactionKey("TicTacToe", 2432))
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Credit).setPlayer("-1").setAmount(Money.create(Currency.FakeMoney, 50)))
                .addPaymentOperation(new PaymentOperation().setOperation(Operation.Debit).setPlayer("-2").setAmount(Money.create(Currency.FakeMoney, 50)));

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PaymentTransactionUnknownPlayers));

        paymentTransactionOperations.perform(paymentTransaction);
    }

    @Test
    public void testInValidPaymentTransaction() {
        ClembleCasinoOperations player = playerOperations.createPlayer();
        ClembleCasinoOperations anotherPlayer = playerOperations.createPlayer();

        PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionKey(new PaymentTransactionKey("TicTacToe", 2432))
                .setTransactionDate(new Date())
                .setProcessingDate(new Date())
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Credit).setPlayer(player.getPlayer())
                                .setAmount(Money.create(Currency.FakeMoney, 60)))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Debit).setPlayer(anotherPlayer.getPlayer())
                                .setAmount(Money.create(Currency.FakeMoney, 50)));

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PaymentTransactionDebitAndCreditNotMatched));

        paymentTransactionOperations.perform(paymentTransaction);
    }

    @Test
    public void testValidTransaction() {
        ClembleCasinoOperations player = playerOperations.createPlayer();
        ClembleCasinoOperations anotherPlayer = playerOperations.createPlayer();

        PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionKey(new PaymentTransactionKey("TicTacToe", 2432))
                .setTransactionDate(new Date())
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Credit).setPlayer(player.getPlayer())
                                .setAmount(Money.create(Currency.FakeMoney, 50)))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Debit).setPlayer(anotherPlayer.getPlayer())
                                .setAmount(Money.create(Currency.FakeMoney, 50)));

        PaymentTransaction savedPaymentTransaction = paymentTransactionOperations.perform(paymentTransaction);

        assertEquals(savedPaymentTransaction, paymentTransaction);
    }

    @Test
    public void testValidTransactionAccess() {
        String source = "TicTacToe";
        long transactionId = 2323;

        ClembleCasinoOperations player = playerOperations.createPlayer();
        ClembleCasinoOperations anotherPlayer = playerOperations.createPlayer();

        PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionKey(new PaymentTransactionKey(source, transactionId))
                .setTransactionDate(new Date())
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Credit).setPlayer(player.getPlayer())
                                .setAmount(Money.create(Currency.FakeMoney, 50)))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Debit).setPlayer(anotherPlayer.getPlayer())
                                .setAmount(Money.create(Currency.FakeMoney, 50)));

        PaymentTransaction savedPaymentTransaction = paymentTransactionOperations.perform(paymentTransaction);

        assertEquals(savedPaymentTransaction, paymentTransaction);
        assertEquals(paymentTransaction, player.paymentOperations().getPaymentTransaction(source, String.valueOf(transactionId)));
        assertEquals(paymentTransaction, player.paymentOperations().getPaymentTransaction(source, String.valueOf(transactionId)));
    }

    // TODO restore @Test
    public void testInValidTransactionAccess() {
        String source = "TicTacToe";
        long transactionId = 2323;

        ClembleCasinoOperations player = playerOperations.createPlayer();
        ClembleCasinoOperations anotherPlayer = playerOperations.createPlayer();
        ClembleCasinoOperations therdPlayer = playerOperations.createPlayer();

        PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionKey(new PaymentTransactionKey(source, transactionId))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Credit).setPlayer(player.getPlayer())
                                .setAmount(Money.create(Currency.FakeMoney, 50)))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Debit).setPlayer(anotherPlayer.getPlayer())
                                .setAmount(Money.create(Currency.FakeMoney, 50)));

        PaymentTransaction savedPaymentTransaction = paymentTransactionOperations.perform(paymentTransaction);

        assertEquals(savedPaymentTransaction, paymentTransaction);
        assertEquals(paymentTransaction, player.paymentOperations().getPaymentTransaction(source, String.valueOf(transactionId)));
        assertEquals(paymentTransaction, anotherPlayer.paymentOperations().getPaymentTransaction(source, String.valueOf(transactionId)));

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PaymentTransactionAccessDenied));
        assertEquals(paymentTransaction, therdPlayer.paymentOperations().getPaymentTransaction(source, String.valueOf(transactionId)));
    }

    @Test
    @Repeat(100)
    public void testRegistrationTransaction() {
        // Step 1. Creating player
        final ClembleCasinoOperations player = playerOperations.createPlayer();
        // Step 2. Checking account exists
        PaymentTransaction paymentTransaction = CheckDelayUtils.check(new Check<PaymentTransaction>() {
            @Override
            public PaymentTransaction get() {
                return player.paymentOperations().getPaymentTransaction("registration", player.getPlayer());
            }
        }, 10_000);
        Collection<PaymentOperation> associatedOperation = new ArrayList<>();
        for (PaymentOperation paymentOperation : paymentTransaction.getPaymentOperations()) {
            if (paymentOperation.getPlayer().equals(player.getPlayer())) {
                associatedOperation.add(paymentOperation);
            }
        }
        // Step 3. Checking there is at list one operation available
        Assert.assertTrue(associatedOperation.size() > 0);
        for (PaymentOperation operation : associatedOperation)
            assertTrue(player.paymentOperations().getAccount().getMoney(operation.getAmount().getCurrency()).getAmount() >= operation.getAmount().getAmount());
    }

}
