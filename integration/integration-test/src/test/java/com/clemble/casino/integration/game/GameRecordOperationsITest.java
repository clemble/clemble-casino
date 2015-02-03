package com.clemble.casino.integration.game;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.lifecycle.record.GameRecord;
import com.clemble.casino.integration.ClembleIntegrationTest;
import com.clemble.casino.integration.event.EventAccumulator;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.lifecycle.record.EventRecord;
import com.clemble.casino.lifecycle.record.RecordState;
import com.clemble.casino.payment.event.PaymentCompleteEvent;
import com.clemble.casino.payment.event.PaymentEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by mavarazy on 09/03/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ClembleIntegrationTest
public class GameRecordOperationsITest {

    @Autowired
    public GameScenarios gameScenarios;

    @Test
    public void testRoundRecordSaving(){
        List<RoundGamePlayer> sessionPlayers = gameScenarios.round(Game.num);
        // Step 1. Preparing game session listener
        EventAccumulator<PaymentEvent> paymentListener = new EventAccumulator<PaymentEvent>();
        RoundGamePlayer A = sessionPlayers.get(0);
        A.playerOperations().listenerOperations().subscribeToPaymentEvents(paymentListener);
        RoundGamePlayer B = sessionPlayers.get(1);
        A.waitForStart();
        B.waitForStart();
        // Step 2. Make a surrender by player B
        A.perform(new SelectNumberAction(2));
        B.perform(new SelectNumberAction(1));
        // Step 3. Synching players
        B.syncWith(A);
        // Step 4. Checking payment transaction complete
        PaymentCompleteEvent event = paymentListener.waitFor(new EventTypeSelector(PaymentCompleteEvent.class));
        assertNotNull(event);
        // Step 5. Checking transaction key is the same as game construction
        String transactionKey = ((PaymentCompleteEvent) event).getTransactionKey();
        String sessionKey = A.getSessionKey();
        assertEquals(sessionKey, transactionKey);
        // Step 6. Checking game record
        GameRecord AgameRecord = A.playerOperations().gameRecordOperations().get(sessionKey);
        GameRecord BgameRecord = A.playerOperations().gameRecordOperations().get(sessionKey);
        // Step 7. Checking game records are same
        assertEquals(AgameRecord, BgameRecord);
        assertEquals(AgameRecord.getEventRecords().size(), 5);
        Iterator<EventRecord> moveIterator = AgameRecord.getEventRecords().iterator();
        moveIterator.next(); // First event is RoundStartedEvent
        assertEquals(moveIterator.next().getEvent(), new PlayerAction(A.getSessionKey(), A.getPlayer(), new SelectNumberAction(2)));
        moveIterator.next();
        assertEquals(moveIterator.next().getEvent(), new PlayerAction(B.getSessionKey(), B.getPlayer(), new SelectNumberAction(1)));
    }

    @Test
    public void testRecordStateChangesToFinished() {
        List<RoundGamePlayer> sessionPlayers = gameScenarios.round(Game.num);
        // Step 1. Preparing game session listener
        EventAccumulator<PaymentEvent> paymentListener = new EventAccumulator<PaymentEvent>();
        RoundGamePlayer A = sessionPlayers.get(0);
        A.playerOperations().listenerOperations().subscribeToPaymentEvents(paymentListener);
        RoundGamePlayer B = sessionPlayers.get(1);
        A.waitForStart();
        B.waitForStart();
        // Step 2. Make B surrender by player B
        A.giveUp();
        A.syncWith(B);
        // Step 3. Checking game record state is Final
        A.waitForEnd();
        GameRecord finalRecord = A.getRecord();
        assertEquals(finalRecord.getState(), RecordState.finished);
    }

}
