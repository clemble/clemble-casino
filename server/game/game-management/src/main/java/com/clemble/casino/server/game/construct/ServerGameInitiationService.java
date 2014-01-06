package com.clemble.casino.server.game.construct;

import java.util.Collection;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.ServerService;

public interface ServerGameInitiationService extends ServerService {

    /**
     * Checks that all parties available, before starting the game.
     * 
     * @param initiation
     */
    public void register(GameInitiation initiation);

    /**
     * Starts game right away, if it fails, it fails.
     * 
     * @param initiation
     */
    public void start(GameInitiation initiation);

    /**
     * Confirms player is connected and ready to start the games
     * 
     * @param session game session
     * @param player confirming player
     * @return appropriate initiation
     */
    public GameInitiation ready(GameSessionKey session, String player);

    /**
     * Returns list of pending initiations for the player
     * 
     * @param game - Game to be queried
     * @param player - player to be queried
     * @return List of GameInitiations for player
     */
    public Collection<GameInitiation> pending(Game game, String player);

}
