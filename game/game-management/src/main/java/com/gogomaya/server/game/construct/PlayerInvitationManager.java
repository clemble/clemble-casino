package com.gogomaya.server.game.construct;

import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.event.schedule.InvitationAcceptedEvent;
import com.gogomaya.server.game.event.schedule.InvitationResponceEvent;
import com.gogomaya.server.game.session.GameSessionRepository;

public class PlayerInvitationManager<State extends GameState> {

    final private GameSessionRepository<State> sessionRepository;

    public PlayerInvitationManager(GameSessionRepository<State> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void playerResponded(InvitationResponceEvent responce) {
        // Step 1. Fetching associated session
        GameSession<State> currentSession = sessionRepository.findOne(responce.getSession());

        if (responce instanceof InvitationAcceptedEvent) {

        }
    }
}
