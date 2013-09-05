package com.gogomaya.server.repository.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogomaya.player.security.PlayerCredential;

@Repository()
public interface PlayerCredentialRepository extends JpaRepository<PlayerCredential, Long> {

    public PlayerCredential findByEmail(String email);

}
