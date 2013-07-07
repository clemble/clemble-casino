package com.gogomaya.server.repository.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.repository.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.player.security.PlayerCredential;

@Repository()
@RestResource(path="playerCredential", exported = true)
public interface PlayerCredentialRepository extends JpaRepository<PlayerCredential, Long> {

    public PlayerCredential findByEmail(String email);

}
