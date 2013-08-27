package com.gogomaya.server.repository.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.player.PlayerProfile;

@Repository
public interface PlayerProfileRepository extends JpaRepository<PlayerProfile, Long>, JpaSpecificationExecutor<PlayerProfile> {

}
