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
import com.gogomaya.server.game.action.GameEngine;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.connection.GameNotificationManager;
import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.game.table.GameTableRepository;

@Controller
public class GameEngineController<State extends GameState> {

    final private GameNotificationManager notificationManager;

    final private GameTableManager<State> tableManager;

    final private GameTableRepository<State> tableRepository;

    final private GameEngine<State> engine;

    public GameEngineController(final GameTableRepository<State> tableRepository,
            final GameEngine<State> engine,
            final GameNotificationManager notificationManager,
            final GameTableManager<State> tableManager) {
        this.notificationManager = checkNotNull(notificationManager);
        this.tableManager = tableManager;
        this.tableRepository = checkNotNull(tableRepository);
        this.engine = checkNotNull(engine);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/active/action", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody GameTable<State> process(@RequestHeader("playerId") long playerId,
            @RequestHeader("sessionId") long sessionId,
            @RequestHeader("tableId") long tableId,
            @RequestBody GameMove move) {
        long startTime = System.nanoTime();
        // Step 1. Retrieving associated table
        GameTable<State> table = tableRepository.findOne(tableId);
        if (table == null)
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        // Step 2. Verifying associated Session identifier
        if (table.getCurrentSession().getSessionId() != sessionId)
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        // Step 3. Updating current state
        State nextState = engine.process(table.getState(), move);
        // Step 3.1 Updating made move
        table.getCurrentSession().addMadeMove(move);
        // Step 3.2
        table.setState(nextState);
        table = tableRepository.saveAndFlush(table);
        if (nextState.complete()) {
            tableManager.setReservable(table);
        }
        // Step 4. Updating listeners
        notificationManager.notify(table);
        return table;
    }

}
