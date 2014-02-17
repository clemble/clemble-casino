package com.clemble.casino.integration.game;

import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.event.server.GamePotEvent;
import com.clemble.casino.integration.game.AbstractGamePlayer;
import com.clemble.casino.integration.game.GamePlayer;
import com.clemble.casino.integration.game.PotGamePlayer;

/**
 * Created by mavarazy on 16/02/14.
 */
public class SimplePotGamePlayer extends AbstractGamePlayer implements PotGamePlayer {

    /**
     * Generated 17/02/14
     */
    private static final long serialVersionUID = -1280382674448825895L;

    final private AtomicReference<PotGameContext> potContext = new AtomicReference<>();

    public SimplePotGamePlayer(ClembleCasinoOperations player, GameConstruction construction) {
        super(player, construction);
        player.listenerOperations().subscribe(new EventTypeSelector(GamePotEvent.class), new EventListener<GamePotEvent>() {
            @Override
            public void onEvent(GamePotEvent event) {
                potContext.set(event.getContext());
            }
        });
    }

    public int getVersion(){
        return potContext.get() != null ? -1 : potContext.get().getOutcomes().size();
    }

    @Override
    public void giveUp() {
    }

    @Override
    public GamePlayer getСurrent() {
        return null;
    }
}
