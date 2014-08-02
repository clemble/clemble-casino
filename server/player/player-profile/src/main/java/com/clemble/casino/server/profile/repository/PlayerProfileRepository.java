package com.clemble.casino.server.profile.repository;

import java.util.List;

import com.clemble.casino.player.PlayerProfile;
import org.springframework.data.neo4j.repository.CRUDRepository;
import org.springframework.data.repository.CrudRepository;

public interface PlayerProfileRepository {

    public PlayerProfile findOne(String player);

    public List<PlayerProfile> findAll(Iterable<String> player);

    public PlayerProfile save(PlayerProfile playerProfile);

    public void deleteAll();

    public long count();

}
