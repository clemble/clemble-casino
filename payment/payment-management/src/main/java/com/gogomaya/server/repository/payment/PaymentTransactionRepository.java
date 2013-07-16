package com.gogomaya.server.repository.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.payment.PaymentTransactionId;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, PaymentTransactionId> {

    public List<PaymentTransaction> findByWalletOperationsPlayerId(long player);

}
