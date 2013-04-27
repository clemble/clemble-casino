package com.gogomaya.server.player.wallet;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.rest.repository.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
@RestResource(path="playerWallet", exported = true)
public interface PlayerWalletRepository extends JpaRepository<PlayerWallet, Long> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public PlayerWallet findOne(Long playerId);
}
