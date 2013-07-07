package com.gogomaya.server.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.server.game.construct.GameConstruction;

@Repository
public interface GameConstructionRepository extends JpaRepository<GameConstruction, Long> {

    @Modifying
    @Transactional
    @Query(value = "update GameConstruction construction set construction.session = :session where construction.construction = :construction")
    public void specifySession(@Param("construction") Long construction, @Param("session") Long sessionId);

    public GameConstruction findBySession(long session);

}
