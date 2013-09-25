package com.gogomaya.server.repository.game;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.construct.GameConstruction;

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
