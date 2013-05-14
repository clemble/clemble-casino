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
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.connection.GameNotificationService;
import com.gogomaya.server.game.outcome.GameOutcomeService;
import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.game.table.GameTableRepository;

@Controller
public class GameEngineController<State extends GameState> {

    final private GameNotificationService notificationManager;

    final private GameTableManager<State> tableManager;

    final private GameTableRepository<State> tableRepository;

    final private GameOutcomeService<State> outcomeService;

    public GameEngineController(final GameTableRepository<State> tableRepository,
            final GameNotificationService notificationManager,
            final GameTableManager<State> tableManager,
            final GameOutcomeService<State> outcomeService) {
        this.notificationManager = checkNotNull(notificationManager);
        this.tableManager = tableManager;
        this.tableRepository = checkNotNull(tableRepository);
        this.outcomeService = checkNotNull(outcomeService);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.POST, value = "/active/action", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    GameTable<State> process(
            @RequestHeader("playerId") long playerId,
            @RequestHeader("sessionId") long sessionId,
            @RequestHeader("tableId") long tableId,
            @RequestBody GameMove move) {
        // Step 1. Retrieving associated table
        GameTable<State> table = tableRepository.findOne(tableId);
        if (table == null)
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        // Step 2. Verifying associated Session identifier
        if (table.getCurrentSession().getSessionId() != sessionId)
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        // Step 3. Updating current state
        State nextState = (State) table.getCurrentSession().getState().process(move);
        // Step 3.1 Updating made move
        table.getCurrentSession().setState(nextState);
        table.getCurrentSession().addMadeMove(move);
        if (nextState.complete()) {
            nextState.increaseVersion();
            outcomeService.finished(table);
            tableRepository.saveAndFlush(table);

            notificationManager.notify(table);

            table.clear();
            tableRepository.saveAndFlush(table);
            tableManager.addReservable(table);
        } else {
            table = tableRepository.saveAndFlush(table);
            // Step 4. Updating listeners
            notificationManager.notify(table);
        }
        return table;
    }

}
