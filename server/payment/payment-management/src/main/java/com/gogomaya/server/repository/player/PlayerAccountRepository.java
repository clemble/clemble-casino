package com.gogomaya.server.repository.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogomaya.payment.PlayerAccount;

@Repository
public interface PlayerAccountRepository extends JpaRepository<PlayerAccount, String> {

}
