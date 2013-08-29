package com.gogomaya.server.repository.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogomaya.player.security.PlayerIdentity;

@Repository
public interface PlayerIdentityRepository extends JpaRepository<PlayerIdentity, Long> {
}
