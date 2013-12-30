package com.clemble.casino.server.repository.player;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import net.spy.memcached.internal.OperationFuture;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.utils.CollectionUtils;
import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CouchbasePlayerProfileRepository implements PlayerProfileRepository {

    final private ObjectMapper objectMapper;
    final private CouchbaseClient client;

    public CouchbasePlayerProfileRepository(CouchbaseClient couchbaseTemplate) {
        this.client = checkNotNull(couchbaseTemplate);
        this.objectMapper = new ObjectMapper();
    }

    public CouchbasePlayerProfileRepository(CouchbaseClient couchbaseTemplate, ObjectMapper objectMapper) {
        this.client = checkNotNull(couchbaseTemplate);
        this.objectMapper = objectMapper;
    }

    @Override
    public PlayerProfile findOne(String player) {
        try {
            return objectMapper.readValue(String.valueOf(client.get(player)), PlayerProfile.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerProfile save(PlayerProfile playerProfile) {
        try {
            OperationFuture<Boolean> saveOperation = client.set(playerProfile.getPlayer(), objectMapper.writeValueAsString(playerProfile));
            if (!saveOperation.get())
                throw new RuntimeException("Failed to save PlayerProfile");
        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return playerProfile;
    }

    @Override
    public void deleteAll() {
        // Step 1. Fetching view
        View view = client.getView("player", "all");
        // Step 2. Query the Cluster
        ViewResponse result = client.query(view, new Query());
        for(ViewRow row: result) {
            client.delete(row.getId());
        }
    }

    @Override
    public long count() {
        long total = 0;
        // Step 1. Fetching view
        View view = client.getView("player", "all_count");
        Query query = new Query();
        query.setReduce(true);
        // Step 2. Query the Cluster
        ViewResponse result = client.query(view, query);
        for(ViewRow row: result) {
            total += Long.valueOf(row.getValue());
        }
        return total;
    }

    @Override
    public List<PlayerProfile> findAll(Iterable<String> players) {
        try {
            Map<String, Object> profiles = client.getBulk(players.iterator());
            List<PlayerProfile> resultProfile = new ArrayList<>(profiles.size());
            for(Object profile: profiles.values())
                resultProfile.add(objectMapper.readValue(String.valueOf(profile), PlayerProfile.class));
            return resultProfile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
