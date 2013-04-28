package com.gogomaya.server.game.table;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.connection.GameServerConnection;
import com.gogomaya.server.game.connection.GameServerConnectionManager;
import com.gogomaya.server.game.specification.GameSpecification;

public class GameTableManagerImpl<State extends GameState> implements GameTableManager<State> {

    final private RedisTemplate<byte[], Long> redisTemplate;
    final private GameTableRepository<State> tableRepository;
    final private GameServerConnectionManager serverConnectionManager;

    @Inject
    public GameTableManagerImpl(final RedisTemplate<byte[], Long> redisTemplate,
            final GameTableRepository<State> tableRepository,
            final GameServerConnectionManager serverConnectionManager) {
        this.redisTemplate = checkNotNull(redisTemplate);
        this.tableRepository = checkNotNull(tableRepository);
        this.serverConnectionManager = checkNotNull(serverConnectionManager);
    }

    @Override
    public GameTable<State> poll(final GameSpecification gameSpecification) {
        byte[] key = gameSpecification.getName().toByteArray();
        // Step 1. Fetching associated Set
        BoundSetOperations<byte[], Long> boundSetOperations = redisTemplate.boundSetOps(key);
        // Step 2. Fetching available table
        Long tableId = boundSetOperations.pop();
        GameTable<State> gameTable = null;
        if (tableId != null) {
            // Seat at the table
            gameTable = tableRepository.findOne(tableId);
        }

        if (gameTable == null) {
            // Step 2.1 Fetching connection resource
            GameServerConnection serverConnection = serverConnectionManager.reserve();
            // Step 2.2 Creating new table with provided specification
            gameTable = new GameTable<State>();
            // Step 2.4 Specifying appropriate values
            gameTable.setSpecification(gameSpecification);
            gameTable.setServerResource(serverConnection);
            // Step 2.3 Creating new table with provided specification
            gameTable = tableRepository.save(gameTable);
        }
        // Step 3. Adding a new user and checking if we can state the session
        return gameTable;
    }

    @Override
    public void setReservable(GameTable<State> gameTable) {
        if (gameTable == null)
            throw new IllegalArgumentException("Game table can't be null");
        if (gameTable.getSpecification() == null)
            throw new IllegalArgumentException("Game specification can't be null");
        byte[] key = gameTable.getSpecification().getName().toByteArray();
        // Step 1. Fetching associated Set
        BoundSetOperations<byte[], Long> boundSetOperations = redisTemplate.boundSetOps(key);
        boundSetOperations.add(gameTable.getTableId());

    }

}
