package com.gogomaya.integration.tictactoe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.event.Event;
import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.event.server.GameServerEvent;
import com.gogomaya.server.game.outcome.PlayerWonOutcome;
import com.gogomaya.server.integration.game.GameSessionListener;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.game.construction.GameScenarios;
import com.gogomaya.server.integration.game.tictactoe.PicPacPoeSessionPlayer;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;
import com.gogomaya.server.tictactoe.PicPacPoeState;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
public class MultipleNotificationsTest {

    @Autowired
    public GameScenarios gameScenarios;

    @Autowired
    public GameConstructionOperations<PicPacPoeState> gameOperations;

    public class NotificationsCounter extends GameSessionListener {

        final private Object sync = new Object();

        final public Map<Integer, Integer> notifications = new HashMap<Integer, Integer>();

        @Override
        protected void notify(Event event) {
            synchronized (sync) {
                if (event instanceof GameServerEvent) {
                    @SuppressWarnings("unchecked")
                    GameServerEvent<PicPacPoeState> serverEvent = (GameServerEvent<PicPacPoeState>) event;
                    Integer currentCounter = notifications.get(serverEvent.getState().getVersion());
                    if (currentCounter == null) {
                        notifications.put(serverEvent.getState().getVersion(), 1);
                    } else {
                        notifications.put(serverEvent.getState().getVersion(), currentCounter + 1);
                    }
                }
            }
        }

    }

    @Test
    public void testMultipleNotificationOnTicTacToe() throws InterruptedException {
        List<GameSessionPlayer<PicPacPoeState>> players = gameScenarios.constructGame(Game.pic);
        PicPacPoeSessionPlayer playerA = (PicPacPoeSessionPlayer) players.get(0);
        PicPacPoeSessionPlayer playerB = (PicPacPoeSessionPlayer) players.get(1);

        final AtomicBoolean notifiedTwice = new AtomicBoolean(false);

        final NotificationsCounter notificationcCounter = new NotificationsCounter();
        playerA.getPlayer().listen(playerA.getSession(), notificationcCounter);

        try {
            playerA.syncWith(playerB);
            playerA.select(0, 0);

            playerA.bet(2);
            playerB.bet(1);
            playerB.syncWith(playerA);

            playerB.select(1, 1);

            playerB.bet(1);
            playerB.syncWith(playerA);
            playerA.bet(2);

            playerA.select(2, 2);

            playerA.bet(2);
            playerA.syncWith(playerB);
            playerB.bet(1);
            playerB.syncWith(playerA);

            playerA.syncWith(playerB);

            assertEquals(((PlayerWonOutcome) playerB.getState().getOutcome()).getWinner(), playerA.getPlayer().getPlayerId());
            assertEquals(((PlayerWonOutcome) playerA.getState().getOutcome()).getWinner(), playerA.getPlayer().getPlayerId());
        } finally {
            playerA.close();
            playerB.close();
        }
        assertFalse(notifiedTwice.get());
        assertTrue(notificationcCounter.notifications.size() > 0);
        Thread.sleep(1000);
        for (Integer version : notificationcCounter.notifications.keySet()) {
            assertEquals(Integer.valueOf(1), notificationcCounter.notifications.get(version));
        }

    }
}
