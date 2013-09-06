package com.gogomaya.server.web.management;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.configuration.ResourceLocationService;
import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.player.service.PlayerSessionService;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.repository.player.PlayerSessionRepository;
import com.gogomaya.web.management.ManagementWebMapping;
import com.gogomaya.web.mapping.WebMapping;

@Controller
public class PlayerSessionController implements PlayerSessionService {

    final private ResourceLocationService resourceLocationService;
    final private PlayerSessionRepository sessionRepository;
    final private PlayerStateManager stateManager;

    public PlayerSessionController(final ResourceLocationService resourceLocationService,
            final PlayerSessionRepository sessionRepository,
            final PlayerStateManager stateManager) {
        this.resourceLocationService = checkNotNull(resourceLocationService);
        this.sessionRepository = checkNotNull(sessionRepository);
        this.stateManager = checkNotNull(stateManager);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    PlayerSession create(@PathVariable("playerId") long playerId) {
        // Step 1. Generated player session
        PlayerSession playerSession = new PlayerSession().setPlayerId(playerId);
        // Step 2. Providing result as a Session data
        Date expireTime = stateManager.markAlive(playerId);
        playerSession.setExpirationTime(expireTime);
        playerSession = sessionRepository.saveAndFlush(playerSession);
        // Step 3. Specifying player resources
        playerSession.setResourceLocations(resourceLocationService.getResources(playerId));
        // Step 4. Returning session identifier
        return playerSession;
    }

    @Override
    @RequestMapping(method = RequestMethod.PUT, value = ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    PlayerSession refreshPlayerSession(@PathVariable("playerId") long playerId, @PathVariable("sessionId") long sessionId) {
        // Step 1. Fetching session
        PlayerSession playerSession = getPlayerSession(playerId, sessionId);
        // Step 2. Sanity check
        if (playerSession.expired())
            throw GogomayaException.fromError(GogomayaError.PlayerSessionClosed);
        Date expireTime = stateManager.refresh(playerId);
        playerSession.setExpirationTime(expireTime);
        playerSession = sessionRepository.saveAndFlush(playerSession);
        // Step 3. Specifying player resources
        playerSession.setResourceLocations(resourceLocationService.getResources(playerId));
        // Step 4. Returning refreshed session
        return playerSession;
    }

    @Override
    @RequestMapping(method = RequestMethod.DELETE, value = ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PlayerSession endPlayerSession(@PathVariable("playerId") long playerId, @PathVariable("sessionId") long sessionId) {
        // Step 1. Fetching player session
        PlayerSession playerSession = getPlayerSession(playerId, sessionId);
        if (playerSession.expired())
            throw GogomayaException.fromError(GogomayaError.PlayerSessionClosed);
        // Step 2. Notifying all listeners of the state change
        stateManager.markLeft(playerId);
        // Step 3. Marking player state as ended
        playerSession.markExpired();
        return sessionRepository.saveAndFlush(playerSession);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = ManagementWebMapping.MANAGEMENT_PLAYER_SESSIONS_SESSION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody PlayerSession
    getPlayerSession(@PathVariable("playerId") long playerId, @PathVariable("sessionId") long sessionId) {
        // Step 2. Reading specific session
        PlayerSession playerSession = sessionRepository.findOne(sessionId);
        if (playerSession.getPlayerId() != playerId)
            throw GogomayaException.fromError(GogomayaError.PlayerNotSessionOwner);
        // Step 3. Returning session
        return playerSession;
    }
}
