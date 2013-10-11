package com.clemble.casino.server.repository.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.payment.PlayerAccount;

@Repository
public interface PlayerAccountRepository extends JpaRepository<PlayerAccount, String> {

}
