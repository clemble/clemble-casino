package com.gogomaya.server.player.wallet;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerWalletRepository extends JpaRepository<PlayerWallet, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public PlayerWallet findOne(Long playerId);

}
