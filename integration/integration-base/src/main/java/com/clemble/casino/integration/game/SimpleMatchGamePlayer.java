package com.clemble.casino.integration.game;

import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.event.server.MatchEvent;
import com.clemble.casino.game.specification.GameConfigurationKey;
import com.clemble.test.concurrent.AsyncCompletionUtils;
import com.clemble.test.concurrent.Get;

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

    public SimpleMatchGamePlayer(ClembleCasinoOperations player, GameSessionKey sessionKey, GameConfigurationKey configurationKey, GamePlayerFactory playerFactory) {
        super(player, sessionKey, configurationKey);
        this.playerFactory = playerFactory;
        player.listenerOperations().subscribe(new EventTypeSelector(MatchEvent.class), new EventListener<MatchEvent>() {
            @Override
            public void onEvent(MatchEvent event) {
                setContext(event.getContext());
            }
        });
        this.setContext((MatchGameContext) player.gameActionOperations(sessionKey).getContext());
    }

    private void setContext(MatchGameContext context) {
        // Step 1. Sanity check
        if (context == null)
            return;
        // Step 2. Going through potcontext
        synchronized (lock) {
            if (potContext.get() == null || potContext.get().getOutcomes().size() < context.getOutcomes().size()) {
                final GameSessionKey sessionKey = context.getCurrentSession();
                GameConfigurationKey configurationKey = playerOperations().gameRecordOperations().get(sessionKey).getConfigurationKey();
                currentPlayer.set(playerFactory.construct(playerOperations(), sessionKey, configurationKey));
                potContext.set(context);
                synchronized (versionLock) {
                    versionLock.notifyAll();
                }
            }
        }
    }

    public int getVersion(){
        return potContext.get() == null ? -1 : potContext.get().getOutcomes().size();
    }

    @Override
    public GamePlayer giveUp() {
        playerOperations().gameActionOperations(getSession()).giveUp();
        return this;
    }

    @Override
    public GamePlayer getСurrent() {
        return currentPlayer.get();
    }
}
