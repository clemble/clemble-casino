package com.gogomaya.server.integration.tictactoe;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.game.tictactoe.action.TicTacToeCell;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeBetOnCellMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeSelectCellMove;
import com.gogomaya.server.integration.game.listener.GameListenerControl;
import com.gogomaya.server.integration.player.Player;

public class TicTacToePlayer {

    final private static String ACTION_URL = "/spi/active/action";

    final private String baseUrl;
    final private RestTemplate restTemplate;

    final private Object versionLock = new Object();

    final private AtomicReference<TicTacToeTable> table = new AtomicReference<TicTacToeTable>();
    private Player player;
    private GameListenerControl listenerControl;

    public TicTacToePlayer(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    public Player getPlayer() {
        return player;
    }

    public TicTacToePlayer setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public TicTacToeTable getTable() {
        return checkNotNull(table.get());
    }

    public TicTacToePlayer setTable(TicTacToeTable newTable) {
        synchronized (versionLock) {
            if (this.table.get() == null
                    || (this.table.get().getState() != null ? this.table.get().getState().getVersion() : -1) < (newTable.getState() != null ? newTable
                            .getState().getVersion() : -1)) {
                this.table.set(newTable);
                versionLock.notifyAll();
            }
        }
        return this;
    }

    public GameListenerControl getListenerControl() {
        return listenerControl;
    }

    public TicTacToePlayer setListenerControl(GameListenerControl listenerControl) {
        this.listenerControl = listenerControl;
        return this;
    }

    public void waitVersion(int expectedVersion) {
        if (this.getTable().getState() != null && this.getTable().getState().getVersion() >= expectedVersion)
            return;

        synchronized (versionLock) {
            while (this.getTable().getState() != null && this.getTable().getState().getVersion() < expectedVersion) {
                try {
                    versionLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void select(int row, int column) {
        // Step 1. Generating bet move
        TicTacToeSelectCellMove move = new TicTacToeSelectCellMove(getPlayer().getPlayerId(), TicTacToeCell.create(row, column));
        // Step 2. Performing actual TicTacToeMove
        perform(move);
    }

    public void bet(int ammount) {
        // Step 1. Generating bet move
        TicTacToeMove move = new TicTacToeBetOnCellMove(getPlayer().getPlayerId(), ammount);
        // Step 2. Performing actual TicTacToeMove
        perform(move);
    }

    private void perform(TicTacToeMove action) {
        // Step 1. Initializing headers
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(getPlayer().getPlayerId()));
        header.add("tableId", String.valueOf(getTable().getTableId()));
        header.add("sessionId", String.valueOf(getTable().getCurrentSession().getSessionId()));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        HttpEntity<TicTacToeMove> requestEntity = new HttpEntity<TicTacToeMove>(action, header);
        // Step 3. Rest template generation
        TicTacToeTable updatedTable = restTemplate.exchange(baseUrl + ACTION_URL, HttpMethod.POST, requestEntity, TicTacToeTable.class).getBody();
        // Step 4. Updating table state
        setTable(updatedTable);
    }
}
