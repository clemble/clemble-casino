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
import com.gogomaya.server.game.table.TicTacToeTableRepository;
import com.gogomaya.server.game.tictactoe.action.TicTacToeEngine;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;

@Controller
public class GameController {

    final private TicTacToeTableRepository tableRepository;

    final private TicTacToeEngine engine;

    final private GameNotificationManager notificationManager;

    public GameController(TicTacToeTableRepository tableRepository, TicTacToeEngine engine, GameNotificationManager notificationManager) {
        this.tableRepository = checkNotNull(tableRepository);
        this.engine = checkNotNull(engine);
        this.notificationManager = checkNotNull(notificationManager);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/active/action", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody TicTacToeTable process(
            @RequestHeader("playerId") long playerId,
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
        table.setState(engine.process(table.getState(), move));
        tableRepository.saveAndFlush(table);
        // Step 4. Updating listeners
        notificationManager.notify(table);
        return table;
    }

}
