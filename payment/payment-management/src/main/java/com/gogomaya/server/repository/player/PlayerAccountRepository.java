package com.gogomaya.server.repository.player;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.player.account.PlayerAccount;

@Repository
public interface PlayerAccountRepository extends JpaRepository<PlayerAccount, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public PlayerAccount findOne(Long playerId);

}
