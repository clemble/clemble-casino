package com.clemble.casino.server.repository.game;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameConstruction;

@Repository
@Transactional
public interface GameConstructionRepository extends JpaRepository<GameConstruction, GameSessionKey> {

    @Override
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public <S extends GameConstruction> List<S> save(Iterable<S> entities);

    @Override
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public GameConstruction saveAndFlush(GameConstruction construction);

}
