package com.clemble.casino.integration.payment;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.integration.player.PlayerOperations;
import com.clemble.casino.integration.spring.TestConfiguration;
import com.clemble.casino.integration.util.ClembleCasinoExceptionMatcherFactory;
import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class PaymentTransactionOperationsTest {

    @Autowired
    public PaymentTransactionOperations paymentTransactionOperations;

    @Autowired
    public PlayerOperations playerOperations;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testFakePaymentTransaction() {
        PaymentTransaction paymentTransaction = new PaymentTransaction().setTransactionKey(new PaymentTransactionKey(MoneySource.TicTacToe.name(), 2432))
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
                .setTransactionKey(new PaymentTransactionKey(MoneySource.TicTacToe.name(), 2432))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Credit).setPlayer(player.getPlayer())
                                .setAmount(Money.create(Currency.FakeMoney, 60)))
                .addPaymentOperation(
                        new PaymentOperation().setOperation(Operation.Debit).setPlayer(anotherPlayer.getPlayer())
                                .setAmount(Money.create(Currency.FakeMoney, 50)));

        expectedException.expect(ClembleCasinoExceptionMatcherFactory.fromErrors(ClembleCasinoError.PaymentTransactionInvalid));

        paymentTransactionOperations.perform(paymentTransaction);
    }

    @Test
    public void testValidTransaction() {
        ClembleCasinoOperations player = playerOperations.createPlayer();
        ClembleCasinoOperations anotherPlayer = playerOperations.createPlayer();

        PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionKey(new PaymentTransactionKey(MoneySource.TicTacToe.name(), 2432))
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
        String source = MoneySource.TicTacToe.name();
        long transactionId = 2323;

        ClembleCasinoOperations player = playerOperations.createPlayer();
        ClembleCasinoOperations anotherPlayer = playerOperations.createPlayer();

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
        assertEquals(paymentTransaction, player.paymentOperations().getPaymentTransaction(source, String.valueOf(transactionId)));
    }

    // TODO restore @Test
    public void testInValidTransactionAccess() {
        String source = MoneySource.TicTacToe.name();
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
    public void testRegistrationTransaction() {
        // Step 1. Creating player
        ClembleCasinoOperations player = playerOperations.createPlayer();
        // Step 2. Checking account exists
        PaymentTransaction paymentTransaction = player.paymentOperations().getPaymentTransaction(MoneySource.registration, player.getPlayer());
        Collection<PaymentOperation> associatedOperation = new ArrayList<>();
        for (PaymentOperation paymentOperation : paymentTransaction.getPaymentOperations()) {
            if (paymentOperation.getPlayer().equals(player.getPlayer())) {
                associatedOperation.add(paymentOperation);
            }
        }
        // Step 3. Checking there is at list one operation available
        Assert.assertTrue(associatedOperation.size() > 0);
        for (PaymentOperation operation : associatedOperation)
            assertEquals(player.paymentOperations().getAccount().getMoney(operation.getAmount().getCurrency()), operation.getAmount());
    }

}
