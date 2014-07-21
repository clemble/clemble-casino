package com.clemble.casino.server.web.management;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.event.SystemPlayerLeftEvent;
import com.clemble.casino.server.player.presence.SystemNotificationService;
import static com.clemble.casino.web.player.PlayerWebMapping.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.service.PlayerSessionService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.repository.player.PlayerSessionRepository;
import com.clemble.casino.web.mapping.WebMapping;

@Controller
public class PlayerSessionController implements PlayerSessionService, ExternalController {

    final private PlayerSessionRepository sessionRepository;
    final private SystemNotificationService notificationService;
    final private ServerPlayerPresenceService playerPresenceService;

    public PlayerSessionController(
        final PlayerSessionRepository sessionRepository,
        final ServerPlayerPresenceService playerPresenceService,
        final SystemNotificationService notificationService) {
        this.sessionRepository = checkNotNull(sessionRepository);
        this.playerPresenceService = checkNotNull(playerPresenceService);
        this.notificationService = notificationService;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = PRESENCE_SESSIONS_PLAYER, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerSession create(@PathVariable("playerId") String playerId) {
        // Step 1. Generated player session
        PlayerSession playerSession = new PlayerSession().setPlayer(playerId);
        // Step 2. Providing result as a Session data
        playerSession.setExpirationTime(playerPresenceService.markAvailable(playerId));
        playerSession = sessionRepository.save(playerSession);
        // Step 3. Returning session identifier
        return playerSession;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = PRESENCE_SESSIONS_PLAYER_SESSION, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    PlayerSession refreshPlayerSession(@PathVariable("playerId") String playerId, @PathVariable("sessionId") String sessionId) {
        // Step 1. Fetching session
        PlayerSession playerSession = getPlayerSession(playerId, sessionId);
        // Step 2. Sanity check
        if (playerSession.expired())
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerSessionClosed);
        playerSession.setExpirationTime(playerPresenceService.markOnline(playerId));
        playerSession = sessionRepository.save(playerSession);
        // Step 3. Returning refreshed session
        return playerSession;
    }

    @Override
    @RequestMapping(method = RequestMethod.DELETE, value = PRESENCE_SESSIONS_PLAYER_SESSION)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody void endPlayerSession(@PathVariable("playerId") String player, @PathVariable("sessionId") String sessionId) {
        // Step 1. Fetching player session
        PlayerSession playerSession = getPlayerSession(player, sessionId);
        if (playerSession.expired())
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerSessionClosed);
        // Step 2. Notifying all listeners of the state change
        notificationService.notify(new SystemPlayerLeftEvent(player));
        // Step 3. Marking player state as ended
        playerSession.markExpired();// TODO generalize session handling
        // Step 4. Saving marked session
        sessionRepository.save(playerSession);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PRESENCE_SESSIONS_PLAYER_SESSION, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PlayerSession getPlayerSession(@PathVariable("playerId") String playerId, @PathVariable("sessionId") String sessionId) {
        // Step 2. Reading specific session
        PlayerSession playerSession = sessionRepository.findOne(sessionId);
        if (!playerSession.getPlayer().equals(playerId))
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerNotSessionOwner);
        // Step 3. Returning session
        return playerSession;
    }
}
