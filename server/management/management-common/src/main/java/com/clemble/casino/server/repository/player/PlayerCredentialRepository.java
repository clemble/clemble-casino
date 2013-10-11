package com.clemble.casino.server.repository.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.player.security.PlayerCredential;

@Repository()
public interface PlayerCredentialRepository extends JpaRepository<PlayerCredential, String> {

    public PlayerCredential findByEmail(String email);

}
