package com.gogomaya.server.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.player.wallet.WalletTransaction;
import com.gogomaya.server.player.wallet.WalletTransactionId;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, WalletTransactionId> {
}
