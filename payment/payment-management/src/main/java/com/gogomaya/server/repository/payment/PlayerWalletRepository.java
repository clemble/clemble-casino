package com.gogomaya.server.repository.payment;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.player.wallet.PlayerWallet;

@Repository
public interface PlayerWalletRepository extends JpaRepository<PlayerWallet, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public PlayerWallet findOne(Long playerId);

}
