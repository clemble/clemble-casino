package com.gogomaya.server.web.active.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.connection.GameNotificationManager;
import com.gogomaya.server.game.session.TicTacToeSessionRepository;
import com.gogomaya.server.game.tictactoe.TicTacToeSession;
import com.gogomaya.server.game.tictactoe.action.TicTacToeEngine;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;

public class GameController {

    final private TicTacToeSessionRepository sessionRepository;

    final private TicTacToeEngine engine;

    final private GameNotificationManager notificationManager;

    public GameController(TicTacToeSessionRepository sessionRepository, TicTacToeEngine engine, GameNotificationManager notificationManager) {
        this.sessionRepository = checkNotNull(sessionRepository);
        this.engine = checkNotNull(engine);
        this.notificationManager = checkNotNull(notificationManager);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/active/action", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public TicTacToeTable process(@RequestHeader("playerId") long playerId, @RequestHeader("sessionId") long sessionId, @RequestBody TicTacToeMove move) {
        // Step 1. Retrieving associated table
        TicTacToeSession session = sessionRepository.findOne(sessionId);
        if (session == null)
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        // Step 2. Updating current state
        session.setGameState(engine.process(session.getGameState(), move));
        sessionRepository.saveAndFlush(session);
        // Step 3. Updating listeners
        notificationManager.notify(session.getTable());
        return session.getTable();
    }
}
