package com.clemble.casino.integration.game;

import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.lifecycle.record.GameRecord;
import com.clemble.casino.game.lifecycle.management.MatchGameContext;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.management.event.MatchEvent;
import com.clemble.casino.game.lifecycle.record.service.GameRecordService;
import org.junit.Assert;

/**
 * Created by mavarazy on 16/02/14.
 */
public class SimpleMatchGamePlayer extends AbstractGamePlayer implements MatchGamePlayer {

    /**
     * Generated 17/02/14
     */
    private static final long serialVersionUID = -1280382674448825895L;

    final private Object lock = new Object();

    final private GamePlayerFactory playerFactory;

    final private AtomicReference<GamePlayer> currentPlayer = new AtomicReference<>();
    final private AtomicReference<MatchGameContext> potContext = new AtomicReference<>();

    public SimpleMatchGamePlayer(final ClembleCasinoOperations player, final String sessionKey, GameConfiguration configuration, GamePlayerFactory playerFactory) {
        super(player, sessionKey, configuration);
        this.playerFactory = playerFactory;
        player.listenerOperations().subscribe(new EventTypeSelector(MatchEvent.class), new EventListener<MatchEvent>() {
            @Override
            public void onEvent(MatchEvent event) {
                LOG.debug("received event {}", event);
                setContext(event.getState().getContext());
            }
        });
        this.setContext((MatchGameContext) player.gameActionOperations(sessionKey).getContext());
    }

    private void setContext(final MatchGameContext context) {
        // Step 1. Sanity check
        if (context == null)
            return;
        // Step 2. Going through potcontext
        final String sessionKey = context.getCurrentSession();
        if(sessionKey == null)
            return;
        final GameRecordService recordOperations = playerOperations().gameRecordOperations();
        if (potContext.get() == null || potContext.get().getOutcomes().size() < context.getOutcomes().size()) {
            GameRecord record = recordOperations.get(sessionKey);
            Assert.assertNotNull(context.getSessionKey() + " >> " + sessionKey + " >> not found in DB", record);
            synchronized (lock) {
                if (potContext.get() == null || potContext.get().getOutcomes().size() < context.getOutcomes().size()) {
                    GameConfiguration configuration = record.getConfiguration();
                    GamePlayer newPlayer = playerFactory.construct(playerOperations(), sessionKey, configuration);
                    LOG.debug("constructed new player for >> {}", sessionKey);
                    currentPlayer.set(newPlayer);
                    potContext.set(context);
                    synchronized (versionLock) {
                        versionLock.notifyAll();
                    }
                }
            }
        }
    }

    public int getVersion(){
        return potContext.get() == null ? -1 : potContext.get().getOutcomes().size();
    }

    @Override
    public GamePlayer giveUp() {
        playerOperations().gameActionOperations(getSessionKey()).giveUp();
        return this;
    }

    @Override
    public GamePlayer getСurrent() {
        return currentPlayer.get();
    }
}
