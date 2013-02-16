package com.gogomaya.server.player.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerCredentialRepository extends JpaRepository<PlayerCredential, Long> {

}
