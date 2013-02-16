package com.gogomaya.server.player.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerWalletRepository extends JpaRepository<PlayerWallet, Long> {
}
