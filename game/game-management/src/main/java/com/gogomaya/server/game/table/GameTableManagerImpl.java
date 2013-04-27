package com.gogomaya.server.game.table;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.connection.GameServerConnection;
import com.gogomaya.server.game.connection.GameServerConnectionManager;

public class GameTableManagerImpl<T extends GameTable<?>> implements GameTableManager<T> {

     Class<T> typeInformation;

    final private RedisTemplate<byte[], Long> redisTemplate;

    final private GameTableRepository<T> tableRepository;

    final private GameServerConnectionManager serverConnectionManager;

    @Inject
    public GameTableManagerImpl(final RedisTemplate<byte[], Long> redisTemplate,
            final GameTableRepository<T> tableRepository,
            final GameServerConnectionManager serverConnectionManager,
            final Class<T> targetTable) {
        this.redisTemplate = checkNotNull(redisTemplate);
        this.tableRepository = checkNotNull(tableRepository);
        this.serverConnectionManager = checkNotNull(serverConnectionManager);
        this.typeInformation = checkNotNull(targetTable);
    }

    @Override
    public T poll(final GameSpecification gameSpecification) {
        byte[] key = gameSpecification.getName().toByteArray();
        // Step 1. Fetching associated Set
        BoundSetOperations<byte[], Long> boundSetOperations = redisTemplate.boundSetOps(key);
        // Step 2. Fetching available table
        Long tableId = boundSetOperations.pop();
        T gameTable = null;
        if (tableId != null) {
            // Seat at the table
            gameTable = tableRepository.findOne(tableId);
        }

        if (gameTable == null) {
            // Step 2.1 Fetching connection resource
            GameServerConnection serverConnection = serverConnectionManager.reserve();
            // Step 2.2 Creating new table with provided specification
            try {
                gameTable = (T) typeInformation.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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
    public void setReservable(T gameTable) {
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
