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
import com.gogomaya.server.game.connection.GameNotificationManager;
import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.game.table.TicTacToeTableRepository;
import com.gogomaya.server.game.tictactoe.action.TicTacToeEngine;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;

@Controller
public class GameEngineController {

    final private GameNotificationManager notificationManager;

    final private GameTableManager tableManager;

    final private TicTacToeTableRepository tableRepository;

    final private TicTacToeEngine engine;

    public GameEngineController(final TicTacToeTableRepository tableRepository,
            final TicTacToeEngine engine,
            final GameNotificationManager notificationManager,
            final GameTableManager tableManager) {
        this.notificationManager = checkNotNull(notificationManager);
        this.tableManager = tableManager;
        this.tableRepository = checkNotNull(tableRepository);
        this.engine = checkNotNull(engine);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/active/action", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody TicTacToeTable process(@RequestHeader("playerId") long playerId,
            @RequestHeader("sessionId") long sessionId,
            @RequestHeader("tableId") long tableId,
            @RequestBody TicTacToeMove move) {
        // Step 1. Retrieving associated table
        TicTacToeTable table = tableRepository.findOne(tableId);
        if (table == null)
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        // Step 2. Verifying associated Session identifier
        if (table.getCurrentSession().getSessionId() != sessionId)
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        // Step 3. Updating current state
        TicTacToeState nextState = engine.process(table.getState(), move);
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
