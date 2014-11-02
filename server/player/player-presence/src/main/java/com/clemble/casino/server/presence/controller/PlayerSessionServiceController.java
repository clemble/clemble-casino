package com.clemble.casino.server.presence.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.event.player.SystemPlayerLeftEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import static com.clemble.casino.player.PresenceWebMapping.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.player.PlayerSession;
import com.clemble.casino.player.service.PlayerSessionServiceContract;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.presence.repository.PlayerSessionRepository;

@RestController
public class PlayerSessionServiceController implements PlayerSessionServiceContract, ExternalController {

    final private PlayerSessionRepository sessionRepository;
    final private SystemNotificationService notificationService;
    final private ServerPlayerPresenceService playerPresenceService;

    public PlayerSessionServiceController(
        final PlayerSessionRepository sessionRepository,
        final ServerPlayerPresenceService playerPresenceService,
        final SystemNotificationService notificationService) {
        this.sessionRepository = checkNotNull(sessionRepository);
        this.playerPresenceService = checkNotNull(playerPresenceService);
        this.notificationService = notificationService;
    }

    @RequestMapping(method = RequestMethod.POST, value = PRESENCE_SESSIONS_PLAYER, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public PlayerSession create(@CookieValue("player") String playerId) {
        // Step 1. Generated player session
        PlayerSession playerSession = new PlayerSession().setPlayer(playerId);
        // Step 2. Providing result as a Session data
        playerSession.setExpirationTime(playerPresenceService.markAvailable(playerId));
        playerSession = sessionRepository.save(playerSession);
        // Step 3. Returning session identifier
        return playerSession;
    }

    @RequestMapping(method = RequestMethod.POST, value = PRESENCE_SESSIONS_PLAYER_SESSION, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public PlayerSession refreshPlayerSession(@CookieValue("player") String playerId, @PathVariable("sessionId") String sessionId) {
        // Step 1. Fetching session
        PlayerSession playerSession = getPlayerSession(playerId, sessionId);
        // Step 2. Sanity check
        if (playerSession.expired())
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerSessionClosed, playerId, sessionId);
        playerSession.setExpirationTime(playerPresenceService.markOnline(playerId));
        playerSession = sessionRepository.save(playerSession);
        // Step 3. Returning refreshed session
        return playerSession;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = PRESENCE_SESSIONS_PLAYER_SESSION)
    @ResponseStatus(value = HttpStatus.OK)
    public void endPlayerSession(@CookieValue("player") String player, @PathVariable("sessionId") String sessionId) {
        // Step 1. Fetching player session
        PlayerSession playerSession = getPlayerSession(player, sessionId);
        if (playerSession.expired())
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerSessionClosed, player, sessionId);
        // Step 2. Notifying all listeners of the state change
        notificationService.send(new SystemPlayerLeftEvent(player));
        // Step 3. Marking player state as ended
        playerSession.markExpired();// TODO generalize session handling
        // Step 4. Saving marked session
        sessionRepository.save(playerSession);
    }

    @RequestMapping(method = RequestMethod.GET, value = PRESENCE_SESSIONS_PLAYER_SESSION, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public PlayerSession getPlayerSession(@CookieValue("player") String playerId, @PathVariable("sessionId") String sessionId) {
        // Step 2. Reading specific session
        PlayerSession playerSession = sessionRepository.findOne(sessionId);
        if (!playerSession.getPlayer().equals(playerId))
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerNotSessionOwner, playerId, sessionId);
        // Step 3. Returning session
        return playerSession;
    }
}
