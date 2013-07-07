package com.gogomaya.server.repository.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.repository.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.player.security.PlayerIdentity;

@Repository
@RestResource(path="playerIdentity", exported = true)
public interface PlayerIdentityRepository extends JpaRepository<PlayerIdentity, Long> {
}
