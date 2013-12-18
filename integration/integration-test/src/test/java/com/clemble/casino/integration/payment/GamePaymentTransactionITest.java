package com.clemble.casino.integration.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.integration.game.GameSessionPlayer;
import com.clemble.casino.integration.game.NumberState;
import com.clemble.casino.integration.game.SelectNumberEvent;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.event.FinishedPaymentEvent;
import com.clemble.casino.payment.event.PaymentEvent;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class GamePaymentTransactionITest {

    @Autowired
    public GameScenarios gameScenarios;

    @Test
    public void testAvailabilityConstruction(){
        List<GameSessionPlayer<NumberState>> sessionPlayers = gameScenarios.construct(Game.num);
        // Step 1. Preparing game session listener
        EventAccumulator paymentListener = new EventAccumulator();
        GameSessionPlayer<NumberState> A = sessionPlayers.get(0);
        A.playerOperations().paymentOperations().subscribe(paymentListener);
        GameSessionPlayer<NumberState> B = sessionPlayers.get(1);
        // Step 2. Make a surrender by player B
        A.perform(new SelectNumberEvent(A.getPlayer(), 2));
        B.perform(new SelectNumberEvent(B.getPlayer(), 1));
        // Step 3. Synching players
        B.syncWith(A);
        // Step 4. Checking payment transaction complete
        PaymentEvent event = paymentListener.poll(5, TimeUnit.SECONDS);
        assertNotNull(event);
        assertTrue(event instanceof FinishedPaymentEvent);
        // Step 5. Checking transaction key is the same as game construction
        PaymentTransactionKey transactionKey = ((FinishedPaymentEvent) event).getTransactionKey();
        GameSessionKey sessionKey = A.getSession();
        assertEquals(sessionKey.getSession(), transactionKey.getTransaction());
        assertEquals(sessionKey.getGame().name(), transactionKey.getSource());
    }
    
}
