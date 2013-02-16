package com.gogomaya.server.player.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.player.security.PlayerIdentity;

@Repository
public interface PlayerIdentityRepository extends JpaRepository<PlayerIdentity, Long> {
}
