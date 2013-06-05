package com.gogomaya.server.player.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.repository.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.player.security.PlayerSession;

@Repository
@RestResource(path = "playerSession", exported = true)
public interface PlayerSessionRepository extends JpaRepository<PlayerSession, Long> {

}
