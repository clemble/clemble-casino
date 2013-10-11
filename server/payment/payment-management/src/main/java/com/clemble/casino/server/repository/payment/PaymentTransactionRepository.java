package com.clemble.casino.server.repository.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, PaymentTransactionKey> {

    public List<PaymentTransaction> findByPaymentOperationsPlayer(String player);

}
