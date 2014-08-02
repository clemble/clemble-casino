package com.clemble.casino.server.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.player.security.PlayerCredential;

@Repository()
public interface PlayerCredentialRepository extends MongoRepository<PlayerCredential, String> {

    public PlayerCredential findByEmail(String email);

}
