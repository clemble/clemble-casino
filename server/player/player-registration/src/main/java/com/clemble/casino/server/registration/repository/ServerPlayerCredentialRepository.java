package com.clemble.casino.server.registration.repository;

import com.clemble.casino.server.registration.ServerPlayerCredential;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.registration.PlayerCredential;

@Repository()
public interface ServerPlayerCredentialRepository extends MongoRepository<ServerPlayerCredential, String> {

    public ServerPlayerCredential findByEmail(String email);

}
