package com.gogomaya.server.web.active.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.table.GameTableRepository;

@Controller
public class GameEngineController<State extends GameState> {

    final private GameTableRepository<State> tableRepository;

    final private GameSessionProcessor<State> sessionProcessor;

    public GameEngineController(
            final GameSessionProcessor<State> sessionProcessor,
            final GameTableRepository<State> tableRepository) {
        this.sessionProcessor = checkNotNull(sessionProcessor);
        this.tableRepository = checkNotNull(tableRepository);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/active/action", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody State process(
            @RequestHeader("playerId") long playerId,
            @RequestHeader("sessionId") long sessionId,
            @RequestHeader("tableId") long tableId,
            @RequestBody ClientEvent move) {
        // Step 1. Retrieving associated table
        GameTable<State> table = tableRepository.findOne(tableId);
        if (table == null)
            throw GogomayaException.fromError(GogomayaError.ServerCriticalError);
        // Step 2. Verifying associated Session identifier
        if (table.getCurrentSession().getSession() != sessionId)
            throw GogomayaException.fromError(GogomayaError.ServerCriticalError);
        // Step 3. Updating current state
        return sessionProcessor.process(sessionId, move);
    }

}
