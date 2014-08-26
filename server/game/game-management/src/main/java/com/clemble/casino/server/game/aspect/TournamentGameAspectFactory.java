package com.clemble.casino.server.game.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.TournamentGameContext;
import com.clemble.casino.game.configuration.TournamentGameConfiguration;

public interface TournamentGameAspectFactory<T extends Event> extends GameAspectFactory<T, TournamentGameContext, TournamentGameConfiguration>{

}
