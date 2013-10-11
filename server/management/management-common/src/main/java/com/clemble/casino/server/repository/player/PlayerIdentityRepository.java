package com.clemble.casino.server.repository.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.player.security.PlayerIdentity;

@Repository
public interface PlayerIdentityRepository extends JpaRepository<PlayerIdentity, String> {
}
