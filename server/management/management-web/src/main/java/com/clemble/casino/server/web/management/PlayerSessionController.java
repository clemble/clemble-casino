package com.clemble.casino.server.web.management;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.server.ExternalController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.configuration.ResourceLocationService;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.service.PlayerSessionService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.repository.player.PlayerSessionRepository;
import com.clemble.casino.web.management.ManagementWebMapping;
import com.clemble.casino.web.mapping.WebMapping;

@Controller
public class PlayerSessionController implements PlayerSessionService, ExternalController {

    final private ResourceLocationService resourceLocationService;
    final private PlayerSessionRepository sessionRepository;
    final private ServerPlayerPresenceService playerPresenceService;

    public PlayerSessionController(final ResourceLocationService resourceLocationService, final PlayerSessionRepository sessionRepository,
            final ServerPlayerPresenceService playerPresenceService) {
        this.resourceLocationService = checkNotNull(resourceLocationService);
        this.sessionRepository = checkNotNull(sessionRepository);
        this.playerPresenceService = checkNotNull(playerPresenceService);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerSession create(@PathVariable("playerId") String playerId) {
        // Step 1. Generated player session
        PlayerSession playerSession = new PlayerSession().setPlayer(playerId);
        // Step 2. Providing result as a Session data
        playerSession.setExpirationTime(playerPresenceService.markAvailable(playerId));
        playerSession = sessionRepository.saveAndFlush(playerSession);
        // Step 3. Specifying player resources
        playerSession.setResourceLocations(resourceLocationService.getResources(playerId));
        // Step 4. Returning session identifier
        return playerSession;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    PlayerSession refreshPlayerSession(@PathVariable("playerId") String playerId, @PathVariable("sessionId") long sessionId) {
        // Step 1. Fetching session
        PlayerSession playerSession = getPlayerSession(playerId, sessionId);
        // Step 2. Sanity check
        if (playerSession.expired())
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerSessionClosed);
        playerSession.setExpirationTime(playerPresenceService.markOnline(playerId));
        playerSession = sessionRepository.saveAndFlush(playerSession);
        // Step 3. Specifying player resources
        playerSession.setResourceLocations(resourceLocationService.getResources(playerId));
        // Step 4. Returning refreshed session
        return playerSession;
    }

    @Override
    @RequestMapping(method = RequestMethod.DELETE, value = ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody void endPlayerSession(@PathVariable("playerId") String player, @PathVariable("sessionId") long sessionId) {
        // Step 1. Fetching player session
        PlayerSession playerSession = getPlayerSession(player, sessionId);
        if (playerSession.expired())
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerSessionClosed);
        // Step 2. Notifying all listeners of the state change
        playerPresenceService.markOffline(player);
        // Step 3. Marking player state as ended
        playerSession.markExpired();
        // Step 4. Saving marked session
        sessionRepository.saveAndFlush(playerSession);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PlayerSession getPlayerSession(@PathVariable("playerId") String playerId, @PathVariable("sessionId") long sessionId) {
        // Step 2. Reading specific session
        PlayerSession playerSession = sessionRepository.findOne(sessionId);
        if (!playerSession.getPlayer().equals(playerId))
            throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerNotSessionOwner);
        // Step 3. Returning session
        return playerSession;
    }
}
